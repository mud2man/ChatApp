Name: Chih-Hung.Lu UNI: cl3519

1. How to compile
    1.a $: make

2. How to run
    2.a server: $: java UdpChat -s <server-port>
    2.b client: $: java java UdpChat -c <nick-name> <server-ip> <server-port> <client-port>

3. Algorithms and datastructure
    3.a Serialize and deserilize
    3.b Registeration table
    3,c Protocals

4. Test plan
    I have four shell scripts "server_test.sh", "client_test_namo.sh", "client_test_min.sh" and "client_test_jordan.sh", 
    which can make testing in the same machine more convient. Note that this program can also run in different machen.
   
    3.a Case#1
        1. run server_test.sh in terminal#1
        2. run "client_test_namo.sh" in terminal#2
        3. run "client_test_min.sh" in terminal#3
        4. run "client_test_jordan.sh" in terminal#4
        5. In terminal#2, type "send min Hi min"
        6. In terminal#2, type "send jordan Hi jordan"
        7. In terminal#3, type "send jordan Hi jordan"
        8. In terminal#2, type "dereg namo"
        9. In terminal#3, type "send namo are you alive"
        10. In terminal#4, type "send namo are you alive"
        11. In terminal#2, type "reg namo"

    3.b Case#1 result
        1. terminal#2:
            namo@csee4119:~/ChatApp$ sh client_test_namo.sh 
            [Welcome, You are registered.]
            >>> [Client table updated.]
            >>> [Client table updated.]
            >>> [Client table updated.]
            >>> send min hi min
            >>> [Message received by min.]
            >>> send jordan hi jordan
            >>> [Message received by jordan.]
            >>> dereg namo
            >>> [You are Offline. Bye.]
            >>> reg namo
            >>> [Welcome, You are registered.]
            >>> [Client table updated.]
            >>> [You have messages]
            >>> min: 2017.03.03-00:43:46 are you alive
            >>> jordan: 2017.03.03-00:43:52 are you alive
            >>> ^C[You are Offline. Bye.]
            >>>

        2. terminal#3:
            namo@csee4119:~/ChatApp$ sh client_test_min.sh 
            [Welcome, You are registered.]
            >>> [Client table updated.]
            >>> [Client table updated.]
            >>> namo: Hi min
            >>> send jordan Hi jordan
            >>> [Message received by jordan.]
            >>> [Client table updated.]
            >>> send namo are you alive
            >>> [No ACK from namo, message sent to server.]
            >>> [Messages received by the server and saved]
            >>> [Client table updated.]
            >>> [Client table updated.]
            >>> ^C[You are Offline. Bye.]
            >>>  

        3. terminal#4:
            namo@csee4119:~/ChatApp$ sh client_test_jordan.sh 
            [Welcome, You are registered.]
            >>> [Client table updated.]
            >>> [Client table updated.]
            >>> [Client table updated.]
            >>> namo: Hi jordan
            >>> min: Hi jordan
            >>> [Client table updated.]
            >>> send namo are you alive
            >>> [No ACK from namo, message sent to server.]
            >>> [Messages received by the server and saved]
            >>> [Client table updated.]
            >>> ^C[You are Offline. Bye.]
            >>> 

    3.c Case#2
        1. run server_test.sh in terminal#1
        2. run "client_test_namo.sh" in terminal#2
        3. run "client_test_min.sh" in terminal#3
        4. In terminal#3, type "dereg jordan"
        5. In terminal#1, type "ctl+c"
        6. In terminal#2, type "send min Hi min"

    3.d Case#2 result
        1. terminal#2:
            namo@csee4119:~/ChatApp$ sh client_test_namo.sh 
            [Welcome, You are registered.]
            >>> [Client table updated.]
            >>> [Client table updated.]
            >>> send min hi min
            >>> [No ACK from min, message sent to server.]
            [Client] server response tiomeout on 1-th time
            [Client] server response tiomeout on 2-th time
            [Client] server response tiomeout on 3-th time
            [Client] server response tiomeout on 4-th time
            [Client] server response tiomeout on 5-th time
            >>> [Server not responding]
            >>> [Exiting]
            >>> 

        2. terminal#3:
            namo@csee4119:~/ChatApp$ sh client_test_min.sh 
            [Welcome, You are registered.]
            >>> [Client table updated.]
            >>> [Client table updated.]
            >>> dereg min
            >>> [You are Offline. Bye.]
            >>> 
