package oraProxyTest;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.connection.DatasourceConnectionProvider;

import oracle.jdbc.driver.OracleConnection;

//import org.jboss.seam.Component;
//import org.jboss.seam.ScopeType;
//import org.jboss.seam.contexts.Contexts;

public class ProxyUserConnectionProvider extends DatasourceConnectionProvider {

	private static final String WRAPPED_CONNECTION_NAME = "org.jboss.resource.adapter.jdbc.WrappedConnection";

	private Class<?> wrappedConnectionClass;

	private Method getUnderlyingConnectionMethod;

	public ProxyUserConnectionProvider() throws Exception {
		this.wrappedConnectionClass = getClass().getClassLoader().loadClass(
				WRAPPED_CONNECTION_NAME);
		this.getUnderlyingConnectionMethod = wrappedConnectionClass.getMethod(
				"getUnderlyingConnection", (Class[]) null);

	}

	protected Connection doGetNativeConnection(Connection con)
			throws SQLException {
		try {
			if (this.wrappedConnectionClass.isAssignableFrom(con.getClass())) {
				return (Connection) getUnderlyingConnectionMethod.invoke(con,
						new Object[0]);
			}
			return con;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	@Override
	public void closeConnection(Connection conn) throws SQLException {
		closeProxyConnection(conn);

		super.closeConnection(conn);
	}

	@Override
	public Connection getConnection() throws SQLException {

		final Connection con = super.getConnection();

/*		if (!Contexts.isSessionContextActive()) {
			return con;
		}

		final UsuarioSessao usuarioSessao = (UsuarioSessao) Component
				.getInstance("usuarioSessao", ScopeType.SESSION, false);
*/
		final String[] roles = { "role1" };

		//if (usuarioSessao != null
		//		&& usuarioSessao.getUsuario().getLogin() != null) {
			/*try {
				openOracleProxy(con, usuarioSessao.getUsuario().getLogin(),
						roles, usuarioSessao.getSenhaProxy());
						*/
			try {
				openOracleProxy(con, "prox", roles, "prox");	
			} catch (SQLException e) {
				// ORA-01017: invalid username/password; logon denied
				if (e.getErrorCode() == 1017) {
					con.close();
					throw new IllegalArgumentException(
							"conexaoNegadaPeloProxyAuthentication");
				} else if (e.getErrorCode() == 28150) {
					con.close();
					throw new IllegalArgumentException(
							"usuarioSemPermissaoAcesso");
				}
				con.close();
				throw e;
			}
		//} else {
		//	// System.err.println("Nao possui usuario para proxy!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		//	throw new IllegalArgumentException(
		//			"Nao foi setado o usuário, para acesso ao oracle proxy authenticate");
		//}

		return con;
	}

	// TODO Verificar proxy, abre e fecha a cada transação
	private void openOracleProxy(Connection pConn, String userName,
			String[] roles, String password) throws SQLException {
		try {
			OracleConnection conn = (OracleConnection) doGetNativeConnection(pConn);
			Properties prop = new Properties();
			prop.put(OracleConnection.PROXY_USER_NAME, userName);
			prop.put(OracleConnection.PROXY_ROLES, roles);
			prop.put(OracleConnection.PROXY_USER_PASSWORD, password);
			conn.openProxySession(OracleConnection.PROXYTYPE_USER_NAME, prop);
		} catch (SQLException e) {
			// e.printStackTrace(System.err);
			throw e;
		}
	}

	private void closeProxyConnection(Connection pConn) {
		try {
			OracleConnection conn = (OracleConnection) doGetNativeConnection(pConn);

			if (conn.isProxySession()) {
				conn.close(OracleConnection.PROXY_SESSION);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}