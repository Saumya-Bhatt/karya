CREATE TABLE IF NOT EXISTS tasks (
    id UUID,
    job_id UUID REFERENCES jobs(id),
    partition_key INT NOT NULL,
    status INT,
    created_at TIMESTAMP,
    executed_at TIMESTAMP NULL,
    next_execution_at TIMESTAMP NULL,

    PRIMARY KEY (id, partition_key)
) PARTITION BY HASH (partition_key);

CREATE INDEX idx_tasks_next_execution_at ON tasks(next_execution_at);