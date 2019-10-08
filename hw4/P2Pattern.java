/** P2Pattern class
 *  @author Josh Hug & Vivant Sakore
 */

public class P2Pattern {
    /* Pattern to match a valid date of the form MM/DD/YYYY. Eg: 9/22/2019 */
    public static final String P1 = "([0-9]{2})\\\\([0-9]{2})\\\\([0-9]{4})";

    /** Pattern to match 61b notation for literal IntLists. */
    public static final String P2 = "\"\\\\(((0|[1-9]\\\\d*),(\\\\s*))*(0|[1-9]\\\\d*)\\\\)\"";

    /* Pattern to match a valid domain name. Eg: www.support.facebook-login.com */
    public static final String P3 = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\\\.)+[A-Za-z]{2,6}";

    /* Pattern to match a valid java variable name. Eg: _child13$ */
    public static final String P4 = "^[a-zA-Z_$][a-zA-Z_$0-9]*$";

    /* Pattern to match a valid IPv4 address. Eg: 127.0.0.1 */
    public static final String P5 = "(([0-1]?[0-9]{1,2}\\.)|(2[0-4][0-9]\\.)|(25[0-5]\\.)){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))"; //FIXME: Add your regex here

}
