package hello.servlet.web.frontcontroller.v4;

import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")

public class FrontControllerServletV4 extends HttpServlet {
    private Map<String, ControllerV4> controllerMap = new HashMap<>();

    public FrontControllerServletV4() {
        // 요청 URI와 컨트롤러의 매핑을 설정한다
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 요청 URI를 가져온다
        String requestURI = request.getRequestURI();

        // 요청 URI에 해당하는 컨트롤러를 가져온다
        ControllerV4 controller = controllerMap.get(requestURI);
        if (controller == null) {
            // 요청 URI에 해당하는 컨트롤러가 없으면 404 오류를 반환한다
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Map<String, String> paramMap = createParamMap(request);
        Map<String, Object> model = new HashMap<>();
        // 컨트롤러를 실행하고 논리 뷰 이름을 받는다
        String viewName = controller.process(paramMap, model);
        MyView view = viewResolver(viewName);
        // 뷰를 렌더링한다
        view.render(model, request, response);
    }

    // 논리 뷰 이름을 실제 뷰 경로로 변환하는 메서드
    private static MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    // HttpServletRequest의 파라미터를 맵으로 변환하는 메서드
    private static Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator().forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }


}

