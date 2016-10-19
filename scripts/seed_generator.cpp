#include <iostream>
#include <time.h>
#include <stdlib.h>
#include <cstdlib>
using namespace std;

int main(int argc, char* argv[]) 
{
    srand(time(NULL));
    int n = atoi(argv[1]);
    for (int i = 0; i < n; ++i) {
        int randnumber = rand();
        cout << randnumber << endl;
    }
}
