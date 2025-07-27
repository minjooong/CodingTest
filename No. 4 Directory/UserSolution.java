import java.util.*;

class UserSolution {

	private final static int NAME_MAXLEN	= 6;
	private final static int PATH_MAXLEN	= 1999;
	
//	 The below commented methods are for your reference. If you want 
//	 to use it, uncomment these methods.
//	
//	int mstrcmp(char[] a, char[] b) {
//		int i;
//		for (i = 0; a[i] != '\0'; i++) {
//			if (a[i] != b[i])
//				return a[i] - b[i];
//		}
//		return a[i] - b[i];
//	}
//
//	int mstrncmp(char[] a, char[] b, int len) {
//		for (int i = 0; i < len; i++) {
//			if (a[i] != b[i])
//				return a[i] - b[i];
//		}
//		return 0;
//	}
//
//	int mstrlen(char[] a) {
//		int len = 0;
//
//		while (a[len] != '\0')
//			len++;
//
//		return len;
//	}
//
//	void mstrcpy(char[] dest, char[] src) {
//		int i = 0;
//		while (src[i] != '\0') {
//			dest[i] = src[i];
//			i++;
//		}
//		dest[i] = src[i];
//	}
//
//	void mstrncpy(char[] dest, char[] src, int len) {
//		for (int i = 0; i < len; i++) {
//			dest[i] = src[i];
//		}
//		dest[len] = '\0';
//	}

    class Dir {
        Dir parent;
        String name;
        HashMap<String, Dir> children;
        int childrenNum;

        Dir(String name, Dir parent) {
            this.name = name;
            this.parent = parent;
            this.childrenNum = 0;
            this.children = new HashMap<String,UserSolution.Dir>(30);
        }
    }
    Dir root;
	
	void init(int n) {
        root = new Dir("/", null);
	}
	
	void cmd_mkdir(char[] path, char[] name) {
		String sPath = new String(path, 0, path.length-1);
        String[] dirs = sPath.split("/");

        Dir parent = root;
        parent.childrenNum++;
        for (String dir : dirs) {
            if (dir.isEmpty()) continue;
            parent = parent.children.get(dir);
            parent.childrenNum++;
        }
        String newName = new String(name, 0, name.length-1);
        parent.children.put(newName, new Dir(newName, parent));
    }
	
	void cmd_rm(char[] path) {
        String sPath = new String(path, 0, path.length-1);
        String[] dirs = sPath.split("/");

		Dir target = root;
        for (String dir : dirs) {
            if (dir.isEmpty()) continue;
            target = target.children.get(dir);
        }
        int delNum = target.childrenNum + 1;
        Dir parent = target.parent;
        parent.children.remove(target.name);
        
        while(parent.parent != null) {
            parent.childrenNum -= delNum;
            parent = parent.parent;
        }
        root.childrenNum -= delNum;
	}
	
	void cmd_cp(char[] srcPath, char[] dstPath) {
		String sPath = new String(srcPath, 0, srcPath.length-1);
        String[] sDirs = sPath.split("/");

        String dPath = new String(dstPath, 0, dstPath.length-1);
        String[] dDirs = dPath.split("/");

        Dir source = root;
        for (String dir : sDirs) {
            if (dir.isEmpty()) continue;
            source = source.children.get(dir);
        }

        Dir destination = root;
        destination.childrenNum += source.childrenNum + 1;
        for (String dir : dDirs) {
            if (dir.isEmpty()) continue;
            destination = destination.children.get(dir);
            destination.childrenNum += source.childrenNum + 1;
        }
        destination.children.put(source.name, deepCopy(source, destination));
	}

    Dir deepCopy(Dir node, Dir newParent) {
        Dir copy = new Dir(node.name, newParent);
        copy.childrenNum = node.childrenNum;

        for (Map.Entry<String, Dir> entry : node.children.entrySet()) {
            Dir childCopy = deepCopy(entry.getValue(), copy);
            copy.children.put(entry.getKey(), childCopy);
        }

        return copy;
    }   
	
	void cmd_mv(char[] srcPath, char[] dstPath) {
		String sPath = new String(srcPath, 0, srcPath.length-1);
        String[] sDirs = sPath.split("/");

        String dPath = new String(dstPath, 0, dstPath.length-1);
        String[] dDirs = dPath.split("/");

        Dir source = root;
        for (String dir : sDirs) {
            if (dir.isEmpty()) continue;
            source = source.children.get(dir);
        }

        int delNum = source.childrenNum + 1;
        Dir parent = source.parent;
        parent.children.remove(source.name);
        while(parent.parent != null) {
            parent.childrenNum -= delNum;
            parent = parent.parent;
        }
        root.childrenNum -= delNum;

        Dir destination = root;
        destination.childrenNum += source.childrenNum + 1;
        for (String dir : dDirs) {
            if (dir.isEmpty()) continue;
            destination = destination.children.get(dir);
            destination.childrenNum += source.childrenNum + 1;
        }
        destination.children.put(source.name, source);
        source.parent = destination;
	}
	
	int cmd_find(char[] path) {
		String sPath = new String(path, 0, path.length-1);
        String[] sDirs = sPath.split("/");

        Dir target = root;
        for (String dir : sDirs) {
            if (dir.isEmpty()) continue;
            target = target.children.get(dir);
        }

		return target.childrenNum;
	}
}
