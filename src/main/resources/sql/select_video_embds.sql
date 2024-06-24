WITH video_uuid AS (
    SELECT uuid
    FROM vr.video
    WHERE url = :url
)
SELECT COUNT(uuid) > 0
FROM vr.embeddings
WHERE uuid IN (SELECT uuid FROM video_uuid LIMIT 1);
