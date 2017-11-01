FIND_PROJECT =
SELECT
    *
FROM
    PROJECT
WHERE
    $if(projectName) {PROJECT_NAME LIKE  :%projectName%}
