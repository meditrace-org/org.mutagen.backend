WITH top_audio AS (
    SELECT uuid,
           text_embedding AS w,
           cosineDistance(text_embedding, :target) AS sim
    FROM vr.audio_embeddings
    WHERE uuid IN :uuids
    ORDER BY sim ASC
    LIMIT :audio_limit
    ),
    top_video AS (
SELECT uuid,
    image_embedding AS w,
    cosineDistance(image_embedding, :target) AS sim
FROM vr.embeddings_max
WHERE uuid IN :uuids
ORDER BY sim ASC
    LIMIT :video_limit
    )
SELECT if(a.uuid = toUUID('00000000-0000-0000-0000-000000000000'), v.uuid, a.uuid) AS uuid,
       quantile(0.005)(
                CASE
                    WHEN v.uuid = toUUID('00000000-0000-0000-0000-000000000000') THEN a.sim
                    WHEN a.uuid = toUUID('00000000-0000-0000-0000-000000000000') THEN v.sim
                    ELSE cosineDistance(
                            :target, (
                                         :alpha * if(v.w = [], arrayMap(x -> 0, range(1, length(a.w))), v.w) +
                                         :beta * if(a.w = [], arrayMap(x -> 0, range(1, length(v.w))), a.w)) /
                                     (:alpha + :beta))
                    END
       ) AS sim,
        video.url AS video_url
FROM top_video v
         FULL JOIN top_audio a ON v.uuid = a.uuid
         INNER JOIN vr.video video ON v.uuid = video.uuid
GROUP BY uuid, video_url
ORDER BY sim ASC
    LIMIT :limit;