package la.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import la.bean.CategoryBean;
import la.dao.CategoryDAO;
import la.dao.DAOException;

@WebServlet("/CategoryServlet")
public class CategoryServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 日本語文字化け対策
		request.setCharacterEncoding("UTF-8");
		try {
			CategoryDAO dao = new CategoryDAO();
			// リクエストパラメータのactionを取得
			String action = request.getParameter("action");

			if (action == null || action.length() == 0 || action.equals("top")) {
				// actionの値がtop、またはなしの場合はトップページを表示 

				// リクエストパラメータのpageを取得
				String page = request.getParameter("page");
				if (page == null || page.length() == 0) {
					// リクエストパラメータにpageがない場合は1を設定
					page = "1";
				}

				// カテゴリ一覧ページに遷移
				showCategories(request, response, dao, page);

			} else if (action.equals("regist")) {

				// リクエストパラメータのactionがregistの場合、登録画面を表示するのみ
				gotoPage(request, response, "/addCategory.jsp");

			} else if (action.equals("add")) {
				String categoryName = request.getParameter("name");

				// DAOのカテゴリ登録メソッドを実行
				dao.addCategory(categoryName);

				// カテゴリ一覧ページに遷移
				// 特に指定はないが更新系実行後のページは1ページ目を表示としておく
				showCategories(request, response, dao, "1");

			} else if (action.equals("edit")) {

				// リクエストパラメータのactionがregistの場合
				// 更新対象のコードを取得、リクエストスコープに設定
				// 更新画面を表示
				int code = Integer.parseInt(request.getParameter("code"));
				request.setAttribute("code", code);
				gotoPage(request, response, "/updateCategory.jsp");

			} else if (action.equals("update")) {

				// リクエストパラメータのactionがupdateの場合
				// 更新対象のコードと新カテゴリ名を取得
				int code = Integer.parseInt(request.getParameter("code"));
				String categoryName = request.getParameter("name");

				// DAOのカテゴリ更新メソッドを実行
				dao.updateCategory(code, categoryName);

				// カテゴリ一覧ページに遷移
				// 特に指定はないが更新系実行後のページは1ページ目を表示としておく
				showCategories(request, response, dao, "1");

			} else if (action.equals("delete")) {

				// リクエストパラメータのactionがdeleteの場合
				// 更新対象のコードを取得
				int code = Integer.parseInt(request.getParameter("code"));

				// DAOのカテゴリ削除メソッドを実行
				dao.deleteCategory(code);

				// カテゴリ一覧ページに遷移
				// 特に指定はないが更新系実行後のページは1ページ目を表示としておく
				showCategories(request, response, dao, "1");

			} else {

				// actionの値が上記の分岐に一致しない場合
				// メッセージをリクエストスコープに設定してerrInternal.jspへフォワード
				request.setAttribute("message", "正しく操作してください。");
				gotoPage(request, response, "/errInternal.jsp");
			}
		} catch (DAOException e) {
			e.printStackTrace();
			request.setAttribute("message", "内部エラーが発生しました。");
			gotoPage(request, response, "/errInternal.jsp");
		}
	}

	// カテゴリ一覧画面を表示するためのメソッド
	private void showCategories(HttpServletRequest request, HttpServletResponse response,
			CategoryDAO dao, String page)
			throws DAOException, ServletException, IOException {

		// 現在のページ数をリクエストスコープに設定
		request.setAttribute("currentPage", page);

		// 最終ページ数をリクエストスコープに設定
		setPageEnd(request, dao);

		List<CategoryBean> list = dao.findAllCategory(page);
		// Listをリクエストスコープに入れてJSPへフォーワードする
		request.setAttribute("categories", list);
		gotoPage(request, response, "/categories.jsp");
	}

	// 最後のページ数を取得してリクエストスコープに設定するメソッド
	// カテゴリの全件を取得して計算する
	private void setPageEnd(HttpServletRequest request, CategoryDAO dao)
			throws DAOException, ServletException, IOException {
		int count = 0;
		count = dao.countCategory();
		int pageEnd = count / 3;
		if (count % 3 > 0) {
			pageEnd++;
		}
		request.setAttribute("pageEnd", pageEnd);
	}

	private void gotoPage(HttpServletRequest request,
			HttpServletResponse response, String page) throws ServletException,
			IOException {
		RequestDispatcher rd = request.getRequestDispatcher(page);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}