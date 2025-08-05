struct Result{
    int top;
    int count;
};

using lint = long long;

int min(int x, int y) { return x < y ? x : y; }
int max(int x, int y) { return x < y ? y : x; }

int C;

struct Node {
  int mn, mx;
  lint sum;

  Node operator+(const Node& b) const {
    Node ret;
    ret.mn = min(mn, b.mn);
    ret.mx = max(mx, b.mx);
    ret.sum = sum + b.sum;
    return ret;
  }
};

const int maxn = 1 << 20;
struct segt {
  Node t[maxn*2];
  lint lazy[maxn*2];

  void update_value(int s, int e, int x, int v) {
    t[x].mn += v;
    t[x].mx += v;
    t[x].sum += (e - s + 1) * (lint)v;
    lazy[x] += v;
  }

  void propagate(int s, int e, int x) {
    if (s == e) return;

    int m = (s + e) / 2;
    update_value(s, m, 2 * x, lazy[x]);
    update_value(m + 1, e, 2 * x + 1, lazy[x]);

    lazy[x] = 0;
  }
  void update(int s, int e, int x, int l, int r, int v) {
    propagate(s, e, x);
    if (l <= s and e <= r) {
      update_value(s, e, x, v);
    } else if (l <= e and s <= r) {
      int m = (s + e) / 2;
      update(s, m, 2 * x, l, r, v);
      update(m + 1, e, 2 * x + 1, l, r, v);
      t[x] = t[2 * x] + t[2 * x + 1];
    }
  }
  void update(int l, int r, int v) { update(0, C - 1, 1, l, r, v); }
  
  Node query() { return t[1]; }
} t;

void init(int _C)
{
  C = _C;
  for (int i=0; i<maxn*2; i++) {
    t.t[i] = (Node){0, 0, 0ll};
    t.lazy[i] = 0;
  }
}

Result dropBlocks(int mCol, int mHeight, int mLength)
{
  t.update(mCol, mCol + mLength - 1, mHeight);

  Node q = t.query();

  Result ret;
  ret.top = q.mx - q.mn;
  ret.count = (q.sum - C * (lint)q.mn) % 1000000;
  return ret;
}