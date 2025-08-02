#include <bits/stdc++.h>
using namespace std;
  
const int INF = 1e7;
const int dx[4] = {0, 0, -1, 1}, dy[4] = {-1, 1, 0, 0};
  
int n, hp, maxId, gateId[350][350], removed[201], dist[201], isInQueue[201];
bool chk[350][350], mp[350][350];
vector<pair<int, int>> v[201];
  
void init(int N, int mMaxStamina, int mMap[350][350])
{
    for(int i=1;i<=maxId;i++) removed[i] = 0, v[i].clear();
    n = N, hp = mMaxStamina, maxId = 0;
    for(int i=0;i<N;i++) for(int j=0;j<N;j++) mp[i][j] = mMap[i][j], gateId[i][j] = 0;
}
  
int qx[40040], qy[40040], qc[40040], front, back;
  
void addGate(int mGateID, int mRow, int mCol)
{
    gateId[mRow][mCol] = mGateID, maxId = mGateID;
  
    memset(chk, 0, sizeof chk);
    front = back = 0;
  
    qx[back] = mRow, qy[back] = mCol, qc[back++] = 0, chk[mRow][mCol] = 1;
      
    while(front < back) {
        int x = qx[front], y = qy[front], c = qc[front++];
  
        if(gateId[x][y]) {
            v[gateId[x][y]].push_back({mGateID, c});
            v[mGateID].push_back({gateId[x][y], c});
        }
  
        if(c == hp) continue;
  
        for(int d=0;d<4;d++) {
            int nx = x + dx[d], ny = y + dy[d];
            if(chk[nx][ny] || mp[nx][ny]) continue;
            qx[back] = nx, qy[back] = ny, qc[back++] = c+1, chk[nx][ny] = 1;
        }
    }
}
  
void removeGate(int mGateID)
{
    removed[mGateID] = 1;
}
  
int getMinTime(int mStartGateID, int mEndGateID)
{
    fill(dist+1, dist+maxId+1, INF);
    memset(isInQueue, 0, sizeof isInQueue);
    front = back = 0;
 
    qx[back++] = mStartGateID, isInQueue[mStartGateID] = 1, dist[mStartGateID] = 0;
 
    while(front < back) {
        auto u = qx[front++]; isInQueue[u] = 0;
 
        for(auto [x, y] : v[u]) if(!removed[x] && dist[u]+y < dist[x]) {
            dist[x] = dist[u]+y;
            if(!isInQueue[x]) qx[back++] = x, isInQueue[x] = 1;
        }
    }
 
    return dist[mEndGateID] == INF ? -1 : dist[mEndGateID];
}