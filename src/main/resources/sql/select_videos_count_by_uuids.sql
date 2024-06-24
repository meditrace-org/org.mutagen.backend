SELECT COUNT(DISTINCT uuid)
FROM vr.video
WHERE uuid IN :uuids;