1. # Standard commands and definitions
2. > init
3. <<<
4. D DATE "Date: \w\w\w \w\w\w \d+ \d\d:\d\d:\d\d \d\d\d\d [-+]\d\d\d\d"
5. # A status log header RE.  Captures the commit id in its sole group.
6. D COMMIT_HEAD "commit ([a-f0-9]+)[ \t]*\n(?:Merge:\s+[0-9a-f]{7}\s+[0-9a-f]{7}[ ]*\n)?${DATE}"
7. # A full log entry.  Captures the entry. Assume logs messages don't contain
8. # "==="
9. D COMMIT_LOG "(===[ ]*\ncommit [a-f0-9]+[ ]*\n(?:Merge:\s+[0-9a-f]{7}\s+[0-9a-f]{7}[ ]*\n)?${DATE}[ ]*\n(?:.|\n)*?(?=\Z|\n===))"
10. # An arbitrary line of text (works even with ?s)
11. D ARBLINE "[^\n]*(?=\n|\Z)"
12. # Zero or more arbitrary full lines of text.
13. D ARBLINES "(?:(?:.|\n)*(?:\n|\Z)|\A|\Z)"