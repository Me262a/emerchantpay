-- Step 1: Add a new BIGINT column
ALTER TABLE merchant ADD COLUMN new_id BIGINT;

-- Step 2: Populate the new column, assuming the table isn't too large
-- This will set a sequence of numbers for the new_id column
WITH numbered AS (
    SELECT id, ROW_NUMBER() OVER () as rownum
    FROM merchant
)
UPDATE merchant m SET new_id = n.rownum FROM numbered n WHERE m.id = n.id;

-- Step 3: Drop old UUID column
ALTER TABLE merchant DROP COLUMN id;

-- Step 4: Rename new BIGINT column to original name
ALTER TABLE merchant RENAME COLUMN new_id TO id;

-- Set the new id column as PRIMARY KEY
ALTER TABLE merchant ADD PRIMARY KEY (id);