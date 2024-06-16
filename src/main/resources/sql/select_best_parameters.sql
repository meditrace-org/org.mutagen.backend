SELECT alpha, beta
FROM (
         SELECT alpha, beta, AVG(score) AS score, FALSE AS is_default
         FROM vr.coef c
         WHERE c.score IS NOT NULL
         GROUP BY alpha, beta

         UNION ALL

         SELECT :default_alpha AS alpha, :default_beta AS beta, 0 AS score, TRUE AS is_default
     ) AS with_defaults
ORDER BY
    score DESC,
    is_default DESC
LIMIT 1;
