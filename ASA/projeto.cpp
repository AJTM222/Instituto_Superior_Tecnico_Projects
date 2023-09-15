#include <vector>
#include <iostream>
#include <algorithm>
#include <unordered_set>

using namespace std;

unordered_set <int> intSet;

vector<int> readline(){
    vector<int> vetor;
    int num;

    cin >> num;
    vetor.push_back(num);

    char c;

    while(((c = getchar()) != '\n') && (c != '\t')){
        cin >> num;
        vetor.push_back(num);
        intSet.insert(num);
    }
    return vetor;
}

void problem1(vector<int> v1) {
    int v1size = v1.size();
    vector<vector<int>> table(v1size, vector<int>{});
    vector<vector<int>> paths;
    vector<int> maxSize; 
    int i, j, k;
    for (int num : v1){
        i = lower_bound(maxSize.begin(), maxSize.end(), num) - maxSize.begin();
        k = 1;
        if (i > 0){
            j = upper_bound(table[i - 1].begin(), table[i - 1].end(), -num) - table[i - 1].begin();
            k = paths[i - 1].back() - paths[i - 1][j];
        }
        table[i].push_back(-num);
        int sz = maxSize.size();
        if (i ==  sz){
            maxSize.push_back(num);
            paths.push_back({0, k});
        }
        else{
            maxSize[i] = num;
            paths[i].push_back(paths[i].back() + k);
        }
    }
    cout << maxSize.size() << ' ' << paths.back().back() << endl;
}


int problem2(vector<int> v1,vector<int> v2){
    int v1size = v1.size();
    int v2size = v2.size();
    int size[v2size] = { 0 };

    for (int i=0; i<v1size; i++){
        int isize = 0;
        for (int j=0; j<v2size; j++){

            if (v1[i] > v2[j]){
                if (size[j] > isize){
                isize = size[j];
                }
            }

            else if (v1[i] == v2[j]){
                int n = isize + 1;
                if (n > size[j]){
                    size[j] = n;
                }
            }
        }
    }
 
    int maxsize = 0;
    for (int i=0; i<v2size; i++){
        if (size[i] > maxsize){
           maxsize = size[i];
        }
    }
  
    return maxsize; 
}

int main(){

    int problem;
    vector<int> v1;
    cin >> problem;
    
    if(problem == 1){
        v1 = readline();
        problem1(v1);
    }
    
    else if(problem == 2){
        vector<int> v2, v1Reduced;
        v1 = readline();
        int el;

        cin >> el;
        v2.push_back(el);

        char c;
        while(((c = getchar()) != '\n') && (c != '\t')){
            cin >> el;
            if (intSet.find(el) != intSet.end()){
                v2.push_back(el);
            }
        }
        cout << problem2(v1, v2) << endl;
    }
}