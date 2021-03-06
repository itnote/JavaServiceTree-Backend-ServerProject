package egovframework.com.cmm.util;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.exception.EgovXssException;

public class EgovXssChecker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EgovXssChecker.class);
	
	public static boolean checkerUserXss(HttpServletRequest request, String sUniqId) throws Exception {
		
		boolean bLog = false;
		
		try {
			//@ 공통모듈을 이용한 권한체크
			LoginVO	loginVO = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();

	    	if(bLog){
	    		LOGGER.debug("@Step1. XSS Check uniqId  : {}", sUniqId);
	    		LOGGER.debug("Step2. XSS Session uniqId  : {}", loginVO.getId());
	    		LOGGER.debug("Step3. XSS Session getUniqId  : {}", loginVO.getUniqId());
	    		LOGGER.debug("Step4. XSS Session getAuthorities  : {}", EgovUserDetailsHelper.getAuthorities());
	    	}

			//체크 값에 대한 무결성 체크
			if(sUniqId == null || loginVO.getUniqId() == null ||  loginVO.getUniqId().equals("")){
				throw new EgovXssException("XSS00001", "errors.xss.checkerUser");
			}
			
			//사용자에에 대한 Xss 체크
			if(!sUniqId.equals(loginVO.getUniqId())){
				throw new EgovXssException("XSS00002", "errors.xss.checkerUser");
			}
		//2017.03.03 	조성원 	시큐어코딩(ES)-오류 메시지를 통한 정보노출[CWE-209]	
		} catch(IllegalArgumentException e) {
			LOGGER.error("[IllegalArgumentException] Try/Catch...usingParameters Runing : "+ e.getMessage());
		} catch(Exception e) {
			LOGGER.error("["+e.getClass()+"] Try/Catch...Exception : " + e.getMessage());
		}
		return true;
	}


}
