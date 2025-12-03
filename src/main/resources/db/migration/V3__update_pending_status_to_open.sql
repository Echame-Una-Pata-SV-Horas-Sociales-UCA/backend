-- Update all PENDING status to OPEN
UPDATE complaints SET status = 'OPEN' WHERE status = 'PENDING';
