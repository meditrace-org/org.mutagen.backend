DELETE FROM vr.audio_embeddings
WHERE (length(splitByWhitespace(coalesce(text, ''))) < 5);