SELECT alpha, beta, strategy
FROM (
         SELECT alpha, beta, avg(score) AS score, FALSE AS is_default, strategy
         FROM vr.coef c
         WHERE c.score IS NOT NULL
         GROUP BY alpha, beta, strategy

         UNION ALL

         SELECT :default_alpha AS alpha, :default_beta AS beta, 0 AS score, TRUE AS is_default, 'QUANTILE' as strategy
     ) AS with_defaults
ORDER BY
    score DESC,
    is_default DESC
LIMIT 1;
