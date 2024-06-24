WITH best_scores AS (
    SELECT strategy, max(score) as score
    FROM vr.coef
    GROUP BY strategy
)
SELECT alpha, beta, strategy, score, FALSE AS is_default
FROM (
    SELECT alpha, beta, score, strategy
    FROM vr.coef c
    WHERE c.score IS NOT NULL
) AS with_defaults
RIGHT JOIN best_scores
ON with_defaults.strategy = best_scores.strategy AND with_defaults.score = best_scores.score
GROUP BY strategy, alpha, beta, score