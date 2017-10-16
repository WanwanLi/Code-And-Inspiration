link: http://magpiehall.com/one-step-matrix-multiplication-with-hadoop/

A is an m-by-n matrix; 
B is an n-by-p matrix.
AB is an m-by-p matrix.
AB[i][k]= sum (A[i][j]*B[j][k]) | j=1:n

map(key, value)
{
    // value is (A, i, j, a[i][j])
    // or ("B", j, k, b[j][k])
    if (value[0] == "A")
    {
        i = value[1]
        j = value[2]
        a[i][j] = value[3]
        for (k = 1 to p)
        {
            emit((i, k), (A, j, a[i][j]))
        }
    }
    else
    {
        j = value[1]
        k = value[2]
        b[j][k] = value[3]
        for (i = 1 to m)
        {
            emit((i, k), (B, j, b[j][k]))
        }
    }
}
 
reduce(key, values)
{
    // key is (i, k)
    // values is a list of (A, j, a[i][j]) 
    // or a list of (B, j, b[j][k])
    hash_A = {j: a[i][j] for (A, j, a[i][j])}
    hash_B = {j: b[j][k] for (B, j, b[j][k])}
    result = 0
    for (j = 1 to n)
    {
        result += hash_A[j] * hash_B[j]
    }
    emit(key, result)
}
