//Chase Hunter
//530
//Project 2 - 2/27/2022


import java.util.*;

public class LRParser {
    //3d array that will help us what rule to use and what state to be in
    static String[][] Table = {
            // id   +   *    (     )   $   E    T    F
            { "s5", "", "", "s4", "", "", "1", "2", "3" },
            { "", "s6", "", "", "", "acc", "", "", "" },
            { "", "r2", "s7", "", "r2", "r2", "", "", "" },
            { "", "r4", "r4", "", "r4", "r4", "", "", "" },
            { "s5", "", "", "s4", "", "", "8", "2", "3" },
            { "", "r6", "r6", "", "r6", "r6", "", "", "" },
            { "s5", "", "", "s4", "", "", "", "9", "3" },
            { "s5", "", "", "s4", "", "", "", "", "10" },
            { "", "s6", "", "", "s11", "", "", "", "" },
            { "", "r1", "s7", "", "r1", "r1", "", "", "" },
            { "", "r3", "r3", "", "r3", "r3", "", "", "" },
            { "", "r5", "r5", "", "r5", "r5", "", "", "" },
    };
    //initializing lists
    static String[] gramLeft = {"E", "E", "T", "T", "F", "F"};
    static String[] gramRight = {"E+T", "T", "T*F", "F", "(E)", "id"};
    static ArrayList<String> operator = new ArrayList<>(Arrays.asList("id", "+", "*", "(", ")", "$", "E", "T", "F"));
    static Stack<String> stack = new Stack<>();
    static Queue<String> queue = new LinkedList<>();
    //This formats the input submitted by the user into the queue, splitting the id's and operators up
    public static void queueInput(String inputFromUser) {
        for (int i = 0; i < inputFromUser.length(); ) {
            char chars;
            chars = inputFromUser.charAt(i);
            if(Character.isLetter(chars)) {
                queue.add("id");
                i += 2;
            }
            else {
                queue.add(Character.toString(chars));
                i++;
            }
        }
    }
    //This is what will return both the index's of the 3d array and will tell us what rule to use
    public static String actions(String state, String token) {
        int index = 0;
        for(String op: operator) {
            if(op.equals(token)) {
                index = operator.indexOf(op);
                break;
            }
        }
        return (Table[Integer.parseInt(state)][index]);
    }
    //This is how we're going to reduce the grammar to correctly complete the stack
    public static void Rules(String rule) {
        int i = Integer.parseInt(Character.toString(rule.charAt(1))) - 1;
        String left = gramLeft[i];
        String right = gramRight[i];

        if (!Objects.equals(right, "id")) {
            right = Character.toString(right.charAt(0));
        }
        if (!(stack.pop().equals(right))) {
            while (!(stack.pop().equals(right))) { }
        }
        stack.push(left);
        stack.push(actions(stack.get(stack.size() - 2), left));
    }
    //this is our parser and is how we're going to print the stack queue and actions
    public static void parse() {
        String action = "";
        //Always start stack at 0
        stack.add("0");
        while(!action.equals("acc")) {
           String state = stack.peek();
           String token = queue.peek();
           //calling the actions method to find the proper index's for the commands
            action = actions(state, token);
            StringBuilder Stack = new StringBuilder();
            StringBuilder Queue = new StringBuilder();
            for (String s : stack) {
                Stack.append(s);
            }
            for(String individual : queue) {
                Queue.append(individual);
            }
            //formatting for the output
            System.out.printf("%-35s %-35s %-15s%n", Stack, Queue, action);
            //this is how we're going to move things off the queue and into the stack along with the state value
            if(action.charAt(0) == 's') {
                stack.push(queue.poll());
                stack.push(action.substring(1));
            }
            //this runs until actions hit acc, which will be at the end with the $ sign
            else if (!action.equals("acc")) {
                Rules(action);
            }
        }
    }
    //The main which will run our methods and build a parse
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Give a sentence to parse stinky: ");
        queueInput(scan.next());
        System.out.printf("%-35s %-35s %-15s%n", "Stack", "Queue","Action");
        parse();
        scan.close();
    }
}
