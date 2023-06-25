/**
 * This package provides the {@link com.shimizukenta.jsonhub.JsonHub} interface to convert, parse, build JSON(RFC 8259).
 * 
 * <p>
 * The primary interface to use is {@link com.shimizukenta.jsonhub.JsonHub}.
 * </p>
 * <pre>
 * // Example of Use.
 * 
 * public class POJO {
 * 	
 *     public int num;
 *     public String str;
 *     public boolean bool;
 *     public List&lt;String&gt; array;
 *     
 *     public POJO() {
 *         num = 100;
 *         str = "STRING";
 *         bool = true;
 *         array = Arrays.asList("a", "b", "c");
 *     }
 *     
 *     public static void main(String[] args) {
 *     
 *         POJO pojo = new POJO();
 *         String json = JsonHub.fromPojo(pojo).toJson();
 *         System.out.println(json);
 *         
 *         // {"num":100,"str":"STRING","bool":true,"array":["a","b","c"]}
 *     }
 * }
 * </pre>
 * 
 * @author kenta-shimizu
 *
 */
package com.shimizukenta.jsonhub;
