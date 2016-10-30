#include <iostream>
#include <time.h>
#include <stdlib.h>
#include <cstdlib>
#include <unistd.h>
using namespace std;

int main(int argc, char* argv[]) 
{
    srand(time(NULL)*getpid());
    int n = atoi(argv[1]);
    for (int i = 0; i < n; ++i) {
        int randnumber = rand();
        cout << randnumber << endl;
    }
}
