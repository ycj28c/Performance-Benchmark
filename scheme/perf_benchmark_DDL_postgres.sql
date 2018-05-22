-- initial
Drop table PERF_BENCHMARK_DET_ATT_RS;
Drop table PERF_BENCHMARK_DET_ATT;
Drop table PERF_BENCHMARK_DETAIL;
Drop table PERF_BENCHMARK_SUMMARY;

Drop SEQUENCE SEQ_PERF_BENCHMARK_SUMMARY;
Drop SEQUENCE SEQ_PERF_BENCHMARK_DETAIL;
Drop SEQUENCE SEQ_PERF_BENCHMARK_DET_ATT;
Drop SEQUENCE SEQ_PERF_BENCHMARK_DET_ATT_RS;

-- PERF_BENCHMARK_SUMMARY table
CREATE TABLE perf_benchmark_summary
(
    perf_benchmark_summary_id BIGINT PRIMARY KEY NOT NULL,
    environment VARCHAR(100) NOT NULL,
    adjust_score DOUBLE PRECISION NOT NULL,
    category_name VARCHAR(2000) NOT NULL,
    create_date TIMESTAMP NOT NULL
);

-- PERF_BENCHMARK_DETAIL table
CREATE TABLE perf_benchmark_detail
(
    perf_benchmark_detail_id BIGINT PRIMARY KEY NOT NULL,
    test_name VARCHAR(2000) NOT NULL,
    perf_benchmark_summary_id BIGINT NOT NULL,
    adjust_score DOUBLE PRECISION NOT NULL,
    first_attempt_score DOUBLE PRECISION NOT NULL,
    pass BIGINT NOT NULL,
    fail BIGINT NOT NULL,
    total BIGINT NOT NULL,
    description VARCHAR(2000),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    create_date TIMESTAMP NOT NULL,
    CONSTRAINT perf_benchmark_detail_fk1 FOREIGN KEY (perf_benchmark_summary_id) REFERENCES perf_benchmark_summary (perf_benchmark_summary_id)
);

-- PERF_BENCHMARK_DET_ATT  table
CREATE TABLE perf_benchmark_det_att
(
    perf_benchmark_det_att_id BIGINT PRIMARY KEY NOT NULL,
    attempt_order BIGINT NOT NULL,
    perf_benchmark_detail_id BIGINT NOT NULL,
    score DOUBLE PRECISION NOT NULL,
    status VARCHAR(4000),
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    create_date TIMESTAMP NOT NULL,
    CONSTRAINT perf_benchmark_det_att_fk1 FOREIGN KEY (perf_benchmark_detail_id) REFERENCES perf_benchmark_detail (perf_benchmark_detail_id)
);

-- PERF_BENCHMARK_DET_ATT_RS  table
CREATE TABLE perf_benchmark_det_att_rs
(
    perf_benchmark_det_att_rs_id BIGINT PRIMARY KEY NOT NULL,
    perf_benchmark_det_att_id BIGINT NOT NULL,
    result_key VARCHAR(3000) NOT NULL,
    result_value_second DOUBLE PRECISION NOT NULL,
    goal DOUBLE PRECISION NOT NULL,
    create_date TIMESTAMP NOT NULL,
    CONSTRAINT perf_benchmark_det_att_rs_fk1 FOREIGN KEY (perf_benchmark_det_att_id) REFERENCES perf_benchmark_det_att (perf_benchmark_det_att_id)
);

CREATE SEQUENCE SEQ_PERF_BENCHMARK_SUMMARY MINVALUE 1 MAXVALUE 99999999999999999 INCREMENT BY 1 CACHE 1;
CREATE SEQUENCE SEQ_PERF_BENCHMARK_DETAIL MINVALUE 1 MAXVALUE 99999999999999999 INCREMENT BY 1 CACHE 1;
CREATE SEQUENCE SEQ_PERF_BENCHMARK_DET_ATT MINVALUE 1 MAXVALUE 99999999999999999 INCREMENT BY 1 CACHE 1;
CREATE SEQUENCE SEQ_PERF_BENCHMARK_DET_ATT_RS MINVALUE 1 MAXVALUE 99999999999999999 INCREMENT BY 1 CACHE 1;
