package org.basex.examples.server;

import org.basex.BaseXServer;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.DropDB;
import org.basex.core.cmd.XQuery;
import org.basex.examples.query.RunQueries;
import org.basex.server.ClientSession;

/**
 * This class sends a query to a database server instance.
 * The query result is used to create and query a local database.
 * For more information on server functionalities, see {@link ServerCommands}
 * For more information on local query processing, see {@link RunQueries}.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-10, ISC License
 * @author BaseX Team
 */
public final class ServerAndLocal {
  /** Local database context. */
  static final Context CONTEXT = new Context();
  /** Reference to the client session. */
  static ClientSession session;

  /**
   * Runs the example code.
   * @param args (ignored) command-line arguments
   * @throws Exception exception
   */
  public static void main(final String[] args) throws Exception {

    System.out.println("=== ServerLocalExample ===");

    // ------------------------------------------------------------------------
    // Start server
    BaseXServer server = new BaseXServer();

    // ------------------------------------------------------------------------
    // Create a client session with host name, port, user name and password
    System.out.println("\n* Create a client session.");

    session = new ClientSession("localhost", 1984, "admin", "admin");

    // -------------------------------------------------------------------------
    // Locally cache the result of a server-side query
    System.out.println("\n* Cache server-side query results.");

    String result = send("XQUERY for $x in doc('etc/xml/input.xml') return $x");

    // -------------------------------------------------------------------------
    // Create a local database from the XML result string
    System.out.println("\n* Create a local database.");

    new CreateDB("LocalDB", result).execute(CONTEXT);

    // -------------------------------------------------------------------------
    // Run a query on the locally cached results
    System.out.println("\n* Run a local query:");

    System.out.println(new XQuery("//title").execute(CONTEXT));

    // ------------------------------------------------------------------------
    // Close the client session
    System.out.println("\n* Close the client session.");

    session.close();

    // ------------------------------------------------------------------------
    // Stop the server
    System.out.println("\n* Stop the server:");

    server.stop();

    // ----------------------------------------------------------------------
    // Drop the local database
    System.out.println("\n* Drop the local database.");

    new DropDB("LocalDB").execute(CONTEXT);
  }

  /**
   * Executes the specified command on the server and writes the
   * response to out.
   * Command info is printed to System.out by default.
   * @param command command to be executed
   * @return string result of command
   * @throws BaseXException database exception
   */
  private static String send(final String command) throws BaseXException {

    // ------------------------------------------------------------------------
    // Execute the command
    return session.execute(command);
  }
}