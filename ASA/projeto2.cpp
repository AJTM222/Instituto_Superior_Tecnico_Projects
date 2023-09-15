#include <vector>
#include <iostream>

using namespace std;
int** Table; /* Tabela que contem os pais , o numero de pais e caso o vetor t seja ancestor de um dos vertices dados*/

bool has_cycle= false;

int* visited;

vector<int> common_ancestors;

bool cycle_finder(int v){
    visited[v] = 1;

    for (int u = 0; u < Table[v][2]; u++){
        if (visited[Table[v][u]] == 0){
            if(cycle_finder(Table[v][u])){
                return true;
            }
        }
        else if (visited[Table[v][u]] == 1){
            has_cycle = true;
            return true;
        }
    }
    visited[v] = 2;
    return false;
}

void dfs(int v, int state){
    visited[v] = state;
    if(Table[v][3] == 1){
        common_ancestors.push_back(v);
        Table[v][3] = 2;
    }
    else{
        Table[v][3] = 1;
    }
    for (int u = 0; u < Table[v][2]; u++){
        if(state != visited[Table[v][u]]){
            dfs(Table[v][u], state);
        }
    }
}

void lowest_common_ancestors(int v){
    visited[v] = 5;
    for (int u = 0; u < Table[v][2]; u++){
        Table[Table[v][u]][3] = 0;
        if (visited[Table[v][u]] != 5){        
            lowest_common_ancestors(Table[v][u]);
        }
    }
}

int main() {
    int v1,v2, n, m, x, y;
    cin >> v1 >> v2;
    
    cin >> n >> m;

    Table = new int*[n+1];
    visited = new int[n+1];
    for(int i = 1; i < n+1; i++){
        Table[i] = new int[4];
        Table[i][2] = 0;
        Table[i][3] = 0;
    }
    for(int i = 0; i < m; i++){
        if(scanf("%d %d", &x, &y) == 0){
            printf("0\n");
            return 0;
        }
        Table[y][Table[y][2]] = x;
        Table[y][2] += 1;
        if(Table[y][2] > 2){
            printf("0\n");
            return 0;
        }
    }

    for (int v = n; v > 0; v--) {
        if (visited[v] == 0 && cycle_finder(v)){
            break;
        }
    }
    if(has_cycle == true){
        printf("0\n");
        return 0;
    }

    if(v1 == v2){
        printf("%d \n", v1);
        return 0;
    }

    dfs(v1, 3);
    dfs(v2, 4);
    
    if(common_ancestors.size() == 0){
        printf("-\n");
        return 0;
    }
    
    for (int i : common_ancestors){
        lowest_common_ancestors(i);
    }
    
    for(int i = 1; i <= n; i++){
        if(Table[i][3] == 2){
            printf("%d ", i);
        }
    }
    printf("\n");

    return 0;
}