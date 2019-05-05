
public class LinkedStack implements Stack {
    // 스택 인터페이스를 implements 해서 연결스택에 맞게 구현한다.
    Node top;
    int size;					// 필드로 top을 가리키는 노드 , 스택의 size를 나타내는 인트형 변수를 갖는다.
    // top이 첫노드이므로 삽입과 삭제가 맨앞에서 이루어진다.

    @Override
    public Node peek() {
        // TODO Auto-generated method stub
        if(isEmpty())throw new java.util.NoSuchElementException();
        System.out.println("top : "+top.data);
        return this.top;
    }							// top에 해당하는 node를 반환한다.

    @Override
    public Node pop() {
        // TODO Auto-generated method stub
        if(isEmpty())throw new java.util.NoSuchElementException();
        Node popedNode = this.top;
        this.top = this.top.next;
        size--;
        System.out.println("pop : "+popedNode.data);
        return popedNode;				// top에 있는 노드를 지우고 반환한다.
    }

    @Override
    public void push(int data) {
        // TODO Auto-generated method stub
        this.top = new Node( data ,top);
        size++;
        System.out.println("push : "+data);
    }							// top에 새로운 노드를 생성하고 size 증가.

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return this.size;
    }							// size를 반환한다.

    public Node removesecond() {

        if(size <=1) {
            System.out.println("removesecond() : 사이즈가 1이하여서 2번째 원소가 존재하지 않습니다.");
            return null;
        }						// size가 1이하면 두번째 원소가 없으므로 지우지 못한다.

        else {
            Node secondNode = this.top.next;
            if(size == 2 )
                this.top.next = null;	// size가 2일때 2번째 원소만 null 해준다.
            else
                this.top.next = this.top.next.next;
            // size가 3이상일 때부터는 이메소드를 해주면 1번째노드가 3번째노드를 가리켜
            // 두번째 원소가 지워진다.
            size--;						// 지우고 size 감소.

            System.out.println("removed Node : "+ secondNode.data);
            return secondNode;					// 지운 원소 반환
        }

    }

    public void reverse() {

        Node p = this.top;

        for(; p != null ; p=p.next)
            this.top = new Node (p.data, this.top);
        // 현재 노드가 5->4->3->2->1 이라고 하면 첫번째 반복문 이후
        // 5->5->4->3->2->1 두번째 반복문 이후 4->5->5->4->3->2->1
        // 세번째 반복문 이후 3->4->5->5->4->3->2->1 이렇게 돼서 끝까지 가면
        //1->2->3->4->5->5->4->3->2->1 이된다.
        p=this.top;

        for(int i = 0 ; i< size-1; i++)
            p=p.next;							// size-1번 걸어나가면 size번째에 해당하는 노드에 p가 위치한다.


        p.next = null;						// p.next를 null로하면 해당size 만큼의 노드를 갖게된다.
        // 1->2->3->4->5->(끊어버림)5->4->3->2->1
        System.out.println("reverse()");

    }

    boolean isEmpty() {
        return this.size == 0;
    }

    private String toString(Node x) {
        if (x.next == null)
            return x.data + " ";
        else
            return x.data + toString(x.next);
    }

    public String toString() {
        System.out.print("Current Elemnets of Stack : top");

        if (isEmpty())
            return "[]";
        else
            return "[ " + toString(top) + "]";
    }

    private static class Node {					// 내부클래스형태의 Node

        int data ;
        Node next;
        
        private Node (int data , Node next) { this.data = data;  this.next = next;}
    }

}
