-- Step 1: Add a new BIGINT column
ALTER TABLE merchant.merchant ADD COLUMN new_id BIGSERIAL;

-- Step 2: Populate the new column, assuming the table isn't too large
-- This will set a sequence of numbers for the new_id column, starting from the maximum number in the sequence (if there's any data in the table)
DO $$
    DECLARE
        max_id BIGINT;
    BEGIN
        SELECT COALESCE(MAX(new_id), 0) INTO max_id FROM merchant.merchant;
        WITH numbered AS (
            SELECT id, ROW_NUMBER() OVER () + max_id as rownum
            FROM merchant.merchant
        )
        UPDATE merchant.merchant m SET new_id = n.rownum FROM numbered n WHERE m.id = n.id;
    END $$;

-- Step 3: Drop old id column
ALTER TABLE merchant.merchant DROP COLUMN id;

-- Step 4: Rename new BIGINT column to original name
ALTER TABLE merchant.merchant RENAME COLUMN new_id TO id;

-- Set the new id column as PRIMARY KEY
ALTER TABLE merchant.merchant ADD PRIMARY KEY (id);

-- Since BIGSERIAL is essentially a BIGINT with an attached sequence, the following steps ensure
-- the sequence continues appropriately after the migration:

-- Step 5: Get the max id after the changes
DO $$
    DECLARE
        max_id BIGINT;
    BEGIN
        SELECT COALESCE(MAX(id), 0) INTO max_id FROM merchant.merchant;
        IF max_id > 0 THEN
            PERFORM setval(pg_get_serial_sequence('merchant.merchant', 'id'), max_id);
        END IF;
    END $$;