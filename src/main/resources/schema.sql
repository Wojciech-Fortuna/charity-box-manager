-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2025-06-16 17:49:21.706

-- tables
-- Table: collection_box
CREATE TABLE IF NOT EXISTS collection_box (
                                box_id BIGSERIAL PRIMARY KEY,
                                identifier varchar(50)  NOT NULL,
                                event_id bigint  NULL
);

-- Table: fundraising_event
CREATE TABLE IF NOT EXISTS fundraising_event (
                                   event_id BIGSERIAL PRIMARY KEY,
                                   name varchar(100)  NOT NULL,
                                   currency varchar(3)  NOT NULL,
                                   account_amount decimal(19,2)  NOT NULL
);

-- Table: money
CREATE TABLE IF NOT EXISTS money (
                       money_id BIGSERIAL PRIMARY KEY,
                       currency varchar(3)  NOT NULL,
                       box_id bigint  NOT NULL,
                       amount decimal(19,2)  NOT NULL
);


-- Drop existing constraints if they exist
ALTER TABLE collection_box
    DROP CONSTRAINT IF EXISTS collection_box_fundraising_event;

ALTER TABLE money
    DROP CONSTRAINT IF EXISTS money_collection_box;

-- Add constraints again
ALTER TABLE collection_box
    ADD CONSTRAINT collection_box_fundraising_event
    FOREIGN KEY (event_id)
    REFERENCES fundraising_event (event_id)
    ON DELETE SET NULL;

ALTER TABLE money
    ADD CONSTRAINT money_collection_box
    FOREIGN KEY (box_id)
    REFERENCES collection_box (box_id)
    ON DELETE CASCADE;
-- End of file.

