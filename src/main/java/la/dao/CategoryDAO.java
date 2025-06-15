package la.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import la.bean.CategoryBean;

public class CategoryDAO {
	// URL、ユーザ名、パスワードの準備
	private String url = "jdbc:postgresql:sample";
	private String user = "student";
	private String pass = "himitu";

	public CategoryDAO() throws DAOException {
		try {
			// JDBCドライバの登録
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new DAOException("JDBCドライバの登録に失敗しました。");
		}
	}

	public List<CategoryBean> findAllCategory(String page) throws DAOException {
		// SQL文の作成
		// LIMITを3として3件のみ取得（ページネーション用）
		// OFFSETにプレースホルダを指定してレコード取得行数を変更可能にする
		String sql = "SELECT * FROM category ORDER BY code LIMIT 3 OFFSET ?";

		try (// データベースへの接続
				Connection con = DriverManager.getConnection(url, user, pass);
				// PreparedStatementオブジェクトの取得
				PreparedStatement st = con.prepareStatement(sql);) {

			// プレースホルダに設定するoffsetの値を算出
			// 引数のpageに値がない場合はoffsetは0で実行
			int offset = 0;
			if (page != null && page.length() != 0) {
				int pageNum = Integer.parseInt(page);
				offset = (pageNum - 1) * 3;
			}
			st.setInt(1, offset);

			try (// SQLの実行
					ResultSet rs = st.executeQuery();) {
				// 結果の取得および表示
				List<CategoryBean> list = new ArrayList<CategoryBean>();
				while (rs.next()) {
					int code = rs.getInt("code");
					String name = rs.getString("name");
					CategoryBean bean = new CategoryBean(code, name);
					list.add(bean);
				}
				// カテゴリ一覧をListとして返す
				return list;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの取得に失敗しました。");
		}
	}

	/**
	 * カテゴリの全件数を取得
	 */
	public int countCategory() throws DAOException {
		// SQL文の作成
		String sql = "SELECT count(*) FROM category";

		try (// データベースへの接続
				Connection con = DriverManager.getConnection(url, user, pass);
				// PreparedStatementオブジェクトの取得
				PreparedStatement st = con.prepareStatement(sql);
				// SQLの実行
				ResultSet rs = st.executeQuery();) {
			if (rs.next()) {
				int count = rs.getInt(1);
				return count;
			}
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの取得に失敗しました。");
		}
	}

	/**
	 * 追加処理
	 */
	public int addCategory(String name) throws DAOException {
		// SQL文の作成
		String sql = "INSERT INTO category(name) VALUES(?)";

		try (// データベースへの接続
				Connection con = DriverManager.getConnection(url, user, pass);
				// PreparedStatementオブジェクトの取得
				PreparedStatement st = con.prepareStatement(sql);) {
			// カテゴリ名を指定
			st.setString(1, name);
			// SQLの実行
			int rows = st.executeUpdate();
			return rows;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの操作に失敗しました。");
		}
	}

	/**
	 * 更新処理
	 */
	public int updateCategory(int code, String name) throws DAOException {
		// SQL文の作成
		String sql = "UPDATE category SET name = ? WHERE code = ?";

		try (// データベースへの接続
				Connection con = DriverManager.getConnection(url, user, pass);
				// PreparedStatementオブジェクトの取得
				PreparedStatement st = con.prepareStatement(sql);) {
			// カテゴリ名とコードを指定
			st.setString(1, name);
			st.setInt(2, code);
			// SQLの実行
			int rows = st.executeUpdate();
			return rows;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの操作に失敗しました。");
		}
	}

	/**
	 * 削除処理
	 */
	public int deleteCategory(int code) throws DAOException {
		// SQL文の作成
		String sql = "DELETE FROM category WHERE code = ?";

		try (// データベースへの接続
				Connection con = DriverManager.getConnection(url, user, pass);
				// PreparedStatementオブジェクトの取得
				PreparedStatement st = con.prepareStatement(sql);) {
			// コードを指定
			st.setInt(1, code);
			// SQLの実行
			int rows = st.executeUpdate();
			return rows;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの操作に失敗しました。");
		}
	}
}