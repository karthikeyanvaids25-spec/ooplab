import java.util.*; 
class BookNotAvailableException extends Exception { 
BookNotAvailableException(String s) { 
super(s); 
} 
} 
interface Issuable { 
void issue(String m) throws BookNotAvailableException; 
void returnBook(); 
} 
class Book { 
int id; 
String title, author, issuedTo; 
boolean issued; 
Book(int id, String title, String author) { 
this.id = id; 
this.title = title; 
this.author = author; 
} 
void issue(String m) throws BookNotAvailableException { 
if (issued) 
throw new BookNotAvailableException( 
"Book \"" + title + "\" is already issued to " + issuedTo 
); 
issued = true; 
issuedTo = m; 
} 
void returnBook() { 
issued = false; 
issuedTo = null; 
} 
void display() { 
System.out.println("ID: " + id + " | " + title + " by " + author + " | " + (issued ? "Issued to " + issuedTo : "Available")); 
} 
} 
class Member { 
int id; 
String name; 
ArrayList<Book> books = new ArrayList<>(); 
Member(int id, String name) { 
this.id = id; 
this.name = name; 
} 
} 
public class LibraryManagementSystem { 
static ArrayList<Book> books = new ArrayList<>(); 
static ArrayList<Member> members = new ArrayList<>(); 
static Book findBook(int id) { 
for (Book b : books) 
if (b.id == id) return b; 
return null; 
} 
static Member findMember(int id) { 
for (Member m : members) 
if (m.id == id) return m; 
return null; 
} 
public static void main(String[] args) { 
Scanner sc = new Scanner(System.in); 
int ch; 
do { 
System.out.print( 
"\n1.AddBook 2.AddMember 3.Issue 4.Return " + 
"5.Display 6.MemberBooks 7.Exit\nChoice: " 
); 
ch = Integer.parseInt(sc.nextLine()); 
switch (ch) { 
case 1: 
                    System.out.print("ID, Title, Author: "); 
                    Book b = new Book( 
                        Integer.parseInt(sc.nextLine()), 
                        sc.nextLine(), 
                        sc.nextLine() 
                    ); 
                    books.add(b); 
                    System.out.println("Added: " + b.title); 
                    break; 
 
                case 2: 
                    System.out.print("ID, Name: "); 
                    Member m = new Member( 
                        Integer.parseInt(sc.nextLine()), 
                        sc.nextLine() 
                    ); 
                    members.add(m); 
                    System.out.println("Added: " + m.name); 
                    break; 
 
                case 3: 
                    System.out.print("BookID, MemberID: "); 
                    b = findBook(Integer.parseInt(sc.nextLine())); 
                    m = findMember(Integer.parseInt(sc.nextLine())); 
                    if (b == null || m == null) { 
                        System.out.println("Invalid ID."); 
                        break; 
                    } 
                    try { 
                        b.issue(m.name); 
                        m.books.add(b); 
                        System.out.println( 
                            "Issued \"" + b.title + "\" to " + m.name 
                        ); 
                    } catch (BookNotAvailableException e) { 
                        System.out.println("Exception: " + e.getMessage()); 
                    } 
                    break; 
 
                case 4: 
                    System.out.print("BookID, MemberID: "); 
                    b = findBook(Integer.parseInt(sc.nextLine())); 
                    m = findMember(Integer.parseInt(sc.nextLine())); 
                    if (b == null || m == null) { 
                        System.out.println("Invalid ID."); 
                        break; 
                    } 
                    b.returnBook(); 
                    m.books.remove(b); 
                    System.out.println("Returned \"" + b.title + "\""); 
                    break; 
 
                case 5: 
                    for (Book x : books) 
                        x.display(); 
                    break; 
 
                case 6: 
                    System.out.print("MemberID: "); 
                    m = findMember(Integer.parseInt(sc.nextLine())); 
                    if (m == null) 
                        System.out.println("Not found."); 
                    else 
                        System.out.println( 
                            m.name + "'s books: " + 
                            (m.books.isEmpty() ? "none" : m.books) 
                        ); 
                    break; 
            } 
        }  
while (ch != 7); 
        System.out.println("Exit."); 
        sc.close(); 
    } 
} 