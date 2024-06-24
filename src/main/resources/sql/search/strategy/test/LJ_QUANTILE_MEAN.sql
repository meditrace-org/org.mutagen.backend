WITH
    arrayMap(x -> 0, range(1, 768)) AS zero_emb,
    toUUID('00000000-0000-0000-0000-000000000000') AS zero_uuid,
    top_audio AS (
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
    FROM vr.embeddings_mean
    WHERE uuid IN :uuids
    ORDER BY sim ASC
    LIMIT :video_limit
    )
SELECT if(a.uuid = zero_uuid, v.uuid, a.uuid) AS uuid,
       quantile(0.005)(
                CASE
                    WHEN v.uuid = zero_uuid THEN a.sim
                    WHEN a.uuid = zero_uuid THEN v.sim
                    ELSE cosineDistance(
                            :target, (
                                         :alpha * if(v.w = [], zero_emb, v.w) +
                                         :beta * if(a.w = [], zero_emb, a.w)) /
                                     (:alpha + :beta))
                    END
       ) AS sim,
        video.url AS video_url
FROM top_video v
    LEFT JOIN top_audio a ON v.uuid = a.uuid
    INNER JOIN vr.video video ON v.uuid = video.uuid
GROUP BY uuid, video_url
ORDER BY sim ASC
    LIMIT :limit;