package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.apache.taglibs.standard.lang.jstl.ImplicitObjects.createParamMap;


@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")

public class FrontControllerServletV3 extends HttpServlet {
    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        // 요청 URI와 컨트롤러의 매핑을 설정한다
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 요청 URI를 가져온다
        String requestURI = request.getRequestURI();

        // ControllerV1 controller = new MemberListControllerV1();

        ControllerV3 controller = controllerMap.get(requestURI);
        if (controller == null) {
            // 요청 URI에 해당하는 컨트롤러가 없으면 404 오류를 반환한다
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // 요청 파라미터를 맵으로 변환한다
        Map<String, String> paramMap = createParamMap(request);
        // 컨트롤러의 비즈니스 로직을 실행하고, ModelView 객체를 반환한다
        ModelView mv = controller.process(paramMap);

        // 논리 뷰 이름을 가져온다
        String viewName = mv.getViewName();// 논리이름 new-form
        // 논리 뷰 이름을 실제 뷰 경로로 변환한다
        MyView view = viewResolver(viewName);
        // 뷰를 렌더링한다
        view.render(mv.getModel(), request, response);
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

