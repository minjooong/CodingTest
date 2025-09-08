#include <unordered_map>
#include <algorithm>

using namespace std;

struct Node;

// ID를 Node 포인터로 매핑
unordered_map<int, Node *> mp;

struct Node {
  // 현재 노드의 인원 수
  int val;

  // 현재 노드의 ID
  int ID;

  // 현재 노드의 자식 수
  Node *child[2];

  // 현재 노드의 부모 노드
  Node *parent;

  // ID, 인원 수, 부모 노드 포인터로 Node 생성
  Node(int id, int v, Node *par) {
    ID = id;
    val = v;
    child[0] = child[1] = nullptr;
    parent = par;
    mp[id] = this;
  }

  // 현재 Node를 지우는 destructor
  ~Node() {
    mp.erase(ID);
    for (int i = 0; i < 2; i++) {
      if (child[i])
        delete child[i];
    }
  }

  // 현재 Node의 서브트리의 인원 수 합
  int total() {
    int ret = val;
    for (int i = 0; i < 2; i++) {
      if (child[i])
        ret += child[i]->total();
    }
    return ret;
  }

  // 현재 Node 아래에 입력받은 ID와 인원수를 가지는 노드 삽입
  // 삽입이 불가능한 경우 false 반환
  bool insert(int id, int v) {
    if (child[0] && child[1]) return false;
    if (!child[0]) {
      child[0] = new Node(id, v, this);
    } else {
      child[1] = new Node(id, v, this);
    }
    return true;
  }

  // {분할 개수, 현재 루트노드 포함 서브트리 인원 수 합}
  // 인원 수가 K 초과인 노드가 존재: 분할 개수 == -1
  pair<int, int> get_ans(int K) {
    if (val > K)
      return make_pair(-1, 0);

    // 자식이 없는 경우
    if (!child[0] && !child[1]) {
      return make_pair(0, val);
    }
    // child[1]만 자식으로 가지는 경우
    else if (!child[0]) {
      auto t = child[1]->get_ans(K);
      if (t.first == -1) return make_pair(-1, 0);
      if (t.second + val > K) return make_pair(t.first + 1, val);
      return make_pair(t.first, t.second + val);
    }
    // child[0]만 자식으로 가지는 경우
    else if (!child[1]) {
      auto t = child[0]->get_ans(K);
      if (t.first == -1) return make_pair(-1, 0);
      if (t.second + val > K) return make_pair(t.first + 1, val);
      return make_pair(t.first, t.second + val);
    }
    // 자식이 2개인 경우
    else {
      auto t0 = child[0]->get_ans(K);
      auto t1 = child[1]->get_ans(K);
      if (t0.first == -1 || t1.first == -1)
        return make_pair(-1, 0);
      if (t0.second + t1.second + val <= K)
        return make_pair(t0.first + t1.first, t0.second + t1.second + val);
      if (t0.second + val > K && t1.second + val > K)
        return make_pair(t0.first + t1.first + 2, val);
      if (t0.second + val > K && t1.second + val <= K)
        return make_pair(t0.first + t1.first + 1, t1.second + val);
      if (t0.second + val <= K && t1.second + val > K)
        return make_pair(t0.first + t1.first + 1, t0.second + val);
      return make_pair(t0.first + t1.first + 1,
                       min(t0.second, t1.second) + val);
    }
  }
};

Node *root;

void init(int mId, int mNum) {
  if (root) delete root;
  root = new Node(mId, mNum, nullptr);
  return;
}

int add(int mId, int mNum, int mParent) {
  Node *r = mp[mParent];
  if (!(r->insert(mId, mNum))) return -1;
  return r->total();
}

int remove(int mId) {
  if (mp.find(mId) == mp.end()) return -1;
  Node *r = mp[mId];

  int ret = r->total();
  if (r->parent->child[0] == r) r->parent->child[0] = nullptr;
  if (r->parent->child[1] == r) r->parent->child[1] = nullptr;
  delete r;
  return ret;
}

int reorganize(int M, int K) {
  auto ans = root->get_ans(K);
  if (ans.first == -1) return 0;
  if (ans.second > 0) {
    // 루트를 포함한 서브트리 또한 하나의 서브트리로 계산
    if (ans.first + 1 <= M) return 1;
    else return 0;
  } else {
    if (ans.first <= M) return 1;
    else return 0;
  }
}