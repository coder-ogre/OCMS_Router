/******************************************
*    @author: Andrew Misicko, Truc Chau   *
*     client.c                            *
******************************************/  

#include<stdio.h>
#include<stdlib.h>
#include<sys/types.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<arpa/inet.h>
#include<unistd.h>
#include<string.h>
#include <errno.h>

//Client sets up to connect to server and communicate in main method
int main()
{
  struct sockaddr_in sock;
  int serverSocket=socket(AF_INET,SOCK_STREAM,0);
  char receiveBuff[21],messageFromServer[21];

  memset(&sock, '\0', sizeof(sock));
  memset(&receiveBuff, '\0', sizeof(receiveBuff));

     
  /***Set up base socket***/
  sock.sin_addr.s_addr=inet_addr("127.0.0.1");
  sock.sin_family=AF_INET;
  sock.sin_port= htons(4446);
     
  //Should return a non-negative value -> success to create connection
  if(connect(serverSocket,(struct sockaddr*)&sock,sizeof(sock))>=0)
  {
    printf("Connected to server #%d... \n",serverSocket);
         
    //Get input string from users (should be 10 characters
    memset(receiveBuff,'\0', sizeof(receiveBuff));
    printf("Enter a string has extacly 10 characters: ");
    fgets(receiveBuff,21,stdin); 
       

    //Check to see the input string has 10 characters
    //if not, the user needs to reenter the string. 
   // We subtract 1 from the arrayLength because we don't count the null character at the end of string
    int arrayLength = strlen(receiveBuff)-1;
    while(arrayLength != 10)
    {
      printf("Your input was %d characters long. \nMake sure your input is exactly 10 characters long, please retry entering. \n", arrayLength);
      fgets(receiveBuff,21,stdin); 
      arrayLength = strlen(receiveBuff)-1;
    }
    //Send the message to server
    write(serverSocket,receiveBuff,11);
          
    read(serverSocket,messageFromServer,11); 
    printf("***Message (client): %s***\n",messageFromServer);
    close(serverSocket); 
  }
  else
  {
    printf("Creating socket failed...\n");
  }
  return 0;
}