------------------------------------------
-- This Sql is for reference, already run
------------------------------------------

--Rename the typo sequence name
rename SEQ_PERFE_BENCHMARK_DET_ATT_RS to SEQ_PERF_BENCHMARK_DET_ATT_RS;

--Change to nocache
ALTER SEQUENCE SEQ_PERF_BENCHMARK_SUMMARY NOCACHE;
ALTER SEQUENCE SEQ_PERF_BENCHMARK_DETAIL NOCACHE;
ALTER SEQUENCE SEQ_PERF_BENCHMARK_DET_ATT NOCACHE;
ALTER SEQUENCE SEQ_PERF_BENCHMARK_DET_ATT_RS NOCACHE;

--Test
--select SEQ_PERF_BENCHMARK_DETAIL.nextval from dual;