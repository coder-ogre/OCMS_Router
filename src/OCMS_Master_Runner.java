public class OCMS_Master_Runner {

    private static OCMS_Master_Runner master = new OCMS_Master_Runner();
    private static String[] ipList = new String[4];// hardcode this when you know the IP values, this will function as your "routing table"

    //if routers are called 1, 2, 3, and 4 by their client id's...
    private static int[] rightConnection = {2, 3, 4, 1};
    private static int[] leftConnection = {4, 1, 2, 3};
    /*
     * This is the "routing table".  This means that:
     *     1 is connected to 2 and 4.
     *  2 is connected to 3 and 1
     *  3 is connected to 4 and 2
     *  and 4 is connected to 1 and 3
     */

    private OCMS_Master_Runner() { }

    public static OCMS_Master_Runner getInstance()
    {
        return master;
    }

    public static void addIp(int index, String ip)
    {
        ipList[index] = ip;
    }

    public static String getIp(int index)
    {
        return ipList[index];
    }

    public static int getRightConnection(int id)
    {
        return rightConnection[id];
    }

    public static int getLeftConnection(int id)
    {
        return leftConnection[id];
    }

    public static String[] getIpList()
    {
        return ipList;
    }

    public static void main(String[] args)
    {
        OCMS_Router r1 = new OCMS_Router(1, ipList);
        r1.start();
    }
}