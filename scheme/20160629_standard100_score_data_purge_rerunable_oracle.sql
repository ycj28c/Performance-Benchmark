-------------------------------
-- PERF_BENCHMARK_DET_ATT_RS 
-------------------------------
-- no change
select * from PERF_BENCHMARK_DET_ATT_RS;

-------------------------------
-- PERF_BENCHMARK_DET_ATT
-------------------------------
-- backup
begin
	execute immediate 'drop table PERF_BENCHMARK_DET_ATT_COPY';
	exception when others then null;
end;
create table PERF_BENCHMARK_DET_ATT_COPY as select * from PERF_BENCHMARK_DET_ATT;

select * from PERF_BENCHMARK_DET_ATT;
select * from PERF_BENCHMARK_DET_ATT_COPY;

-- update 
UPDATE PERF_BENCHMARK_DET_ATT
SET score = (
	CASE 
		WHEN score > 100 THEN 100
		ELSE score
	END);
	
commit;

-- clean trash
drop table PERF_BENCHMARK_DET_ATT_COPY;

-------------------------------
-- PERF_BENCHMARK_DETAIL
-------------------------------
-- backup
begin
	execute immediate 'drop table PERF_BENCHMARK_DETAIL_COPY';
	exception when others then null;
end;
create table PERF_BENCHMARK_DETAIL_COPY as select * from PERF_BENCHMARK_DETAIL;

select * from PERF_BENCHMARK_DETAIL;
select * from PERF_BENCHMARK_DETAIL_COPY;

-- update score for network test
update PERF_BENCHMARK_DETAIL set ADJUST_SCORE = (
    case when ADJUST_SCORE >100 then 100
      else ADJUST_SCORE
    end
) where PERF_BENCHMARK_DETAIL_ID in(
  select PERF_BENCHMARK_DETAIL_ID from PERF_BENCHMARK_DETAIL a
  where a.PERF_BENCHMARK_DETAIL_ID
  not in (select PERF_BENCHMARK_DETAIL_ID from PERF_BENCHMARK_DET_ATT)
);

commit;

-- update score for business test
begin
	execute immediate 'drop table PERF_BENCHMARK_DETAIL_TEMP';
	exception when others then null;
end;
create table PERF_BENCHMARK_DETAIL_TEMP as (
	select PERF_BENCHMARK_DETAIL_ID, sum(score)/count(score) as new_score
	from PERF_BENCHMARK_DET_ATT
	group by PERF_BENCHMARK_DETAIL_ID 
	);

update PERF_BENCHMARK_DETAIL a set a.ADJUST_SCORE =
(select b.new_score from PERF_BENCHMARK_DETAIL_TEMP b where a.PERF_BENCHMARK_DETAIL_ID = b.PERF_BENCHMARK_DETAIL_ID)
where PERF_BENCHMARK_DETAIL_ID in (
    select PERF_BENCHMARK_DETAIL_ID from PERF_BENCHMARK_DETAIL a
    where a.PERF_BENCHMARK_DETAIL_ID
    in (select PERF_BENCHMARK_DETAIL_ID from PERF_BENCHMARK_DET_ATT)
);

commit;

-- update the first_attempt_score
update PERF_BENCHMARK_DETAIL
set FIRST_ATTEMPT_SCORE = (
  CASE
	WHEN FIRST_ATTEMPT_SCORE > 100 THEN 100
	ELSE FIRST_ATTEMPT_SCORE
END);

commit;

-- clean temp/backup table
drop table PERF_BENCHMARK_DETAIL_COPY;
drop table PERF_BENCHMARK_DETAIL_TEMP;

-------------------------------
-- PERF_BENCHMARK_SUMMARY
-------------------------------
-- backup
begin
	execute immediate 'drop table PERF_BENCHMARK_SUMMARY_COPY';
	exception when others then null;
end;
create table PERF_BENCHMARK_SUMMARY_COPY as select * from PERF_BENCHMARK_SUMMARY;

select * from PERF_BENCHMARK_SUMMARY;
select * from PERF_BENCHMARK_SUMMARY_COPY;

-- create temp table for weight algorithm
begin
	execute immediate 'drop table PERF_BENCHMARK_SUMMARY_TEMP';
	exception when others then null;
end;
create table PERF_BENCHMARK_SUMMARY_TEMP as (
select
  new_id,
  case
    when sum("cnt") = 0 then -1
    else sum("float score")/sum("cnt")
  END as new_score,
  sum("float score") as FLOATSCORE,
  sum("cnt") as CNT
   from (
  SELECT
    a.PERF_BENCHMARK_SUMMARY_ID new_id,
    b.PERF_BENCHMARK_SUMMARY_ID,
    a.ADJUST_SCORE,
    b.ADJUST_SCORE,
    CASE
    WHEN a.adjust_score >= 90 AND a.adjust_score <= 100
      THEN a.adjust_score * 1
    WHEN a.adjust_score >= 80 AND a.adjust_score < 90
      THEN a.adjust_score * 2
    WHEN a.adjust_score >= 70 AND a.adjust_score < 80
      THEN a.adjust_score * 3
    WHEN a.adjust_score >= 60 AND a.adjust_score < 70
      THEN a.adjust_score * 4
    WHEN a.adjust_score >= 50 AND a.adjust_score < 60
      THEN a.adjust_score * 5
    WHEN a.adjust_score >= 40 AND a.adjust_score < 50
      THEN a.adjust_score * 6
    WHEN a.adjust_score >= 30 AND a.adjust_score < 40
      THEN a.adjust_score * 7
    WHEN a.adjust_score >= 20 AND a.adjust_score < 30
      THEN a.adjust_score * 8
    WHEN a.adjust_score >= 10 AND a.adjust_score < 20
      THEN a.adjust_score * 9
    ELSE 100
    END AS "float score",
    CASE
    WHEN a.adjust_score >= 90 AND a.adjust_score <= 100
      THEN 1
    WHEN a.adjust_score >= 80 AND a.adjust_score < 90
      THEN 2
    WHEN a.adjust_score >= 70 AND a.adjust_score < 80
      THEN 3
    WHEN a.adjust_score >= 60 AND a.adjust_score < 70
      THEN 4
    WHEN a.adjust_score >= 50 AND a.adjust_score < 60
      THEN 5
    WHEN a.adjust_score >= 40 AND a.adjust_score < 50
      THEN 6
    WHEN a.adjust_score >= 30 AND a.adjust_score < 40
      THEN 7
    WHEN a.adjust_score >= 20 AND a.adjust_score < 30
      THEN 8
    WHEN a.adjust_score >= 10 AND a.adjust_score < 20
      THEN 9
    ELSE 1
    END AS "cnt"
  FROM PERF_BENCHMARK_DETAIL a
    INNER JOIN PERF_BENCHMARK_SUMMARY b
      ON a.PERF_BENCHMARK_SUMMARY_ID = b.PERF_BENCHMARK_SUMMARY_ID
) group by new_id );

-- update the summary adjust score
update PERF_BENCHMARK_SUMMARY a
set a.ADJUST_SCORE =
(select new_score from PERF_BENCHMARK_SUMMARY_TEMP b where a.PERF_BENCHMARK_SUMMARY_ID = b.new_id);

commit;

-- clean trash
drop table PERF_BENCHMARK_SUMMARY_COPY;
drop table PERF_BENCHMARK_SUMMARY_TEMP;

-------------------------------
-- DONE
-------------------------------













