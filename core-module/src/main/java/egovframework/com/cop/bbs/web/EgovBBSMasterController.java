package egovframework.com.cop.bbs.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.annotation.IncludedInfo;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.cop.bbs.service.BoardMaster;
import egovframework.com.cop.bbs.service.BoardMasterVO;
import egovframework.com.cop.bbs.service.EgovBBSMasterService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class EgovBBSMasterController {

    @Resource(name = "EgovBBSMasterService")
    private EgovBBSMasterService egovBBSMasterService;

    @Resource(name = "EgovCmmUseService")
    private EgovCmmUseService cmmUseService;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Autowired
    private DefaultBeanValidator beanValidator;

    //Logger log = Logger.getLogger(this.getClass());

    @RequestMapping("/cop/bbs/insertBBSMasterView.do")
    public String insertBBSMasterView(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, ModelMap model) throws Exception {
        BoardMasterVO boardMaster = new BoardMasterVO();

        //공통코드(게시판유형)
        ComDefaultCodeVO vo = new ComDefaultCodeVO();
        vo.setCodeId("COM101");
        List<?> codeResult = cmmUseService.selectCmmCodeDetail(vo);

        model.addAttribute("bbsTyCode", codeResult);
        model.addAttribute("boardMasterVO", boardMaster);


        return "egovframework/com/cop/bbs/EgovBBSMasterRegist";
    }

    @RequestMapping("/cop/bbs/insertBBSMaster.do")
    public String insertBBSMaster(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, @ModelAttribute("boardMaster") BoardMaster boardMaster,
                                  BindingResult bindingResult, ModelMap model) throws Exception {

        //LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
        //Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

        beanValidator.validate(boardMaster, bindingResult);
        if (bindingResult.hasErrors()) {
            ComDefaultCodeVO vo = new ComDefaultCodeVO();

            //게시판유형코드
            vo.setCodeId("COM101");
            List<?> codeResult = cmmUseService.selectCmmCodeDetail(vo);
            model.addAttribute("bbsTyCode", codeResult);

            return "egovframework/com/cop/bbs/EgovBBSMasterRegist";
        }

        //if (isAuthenticated) {
        boardMaster.setFrstRegisterId("313");
//		    boardMaster.setFrstRegisterId(user.getUniqId());
        boardMaster.setUseAt("Y");
        boardMaster.setTrgetId("SYSTEMDEFAULT_REGIST");
        egovBBSMasterService.insertBBSMasterInf(boardMaster);
        //}

        return "forward:/cop/bbs/selectBBSMasterInfs.do";
    }

    @IncludedInfo(name = "게시판관리", order = 180, gid = 40)
    @RequestMapping("/cop/bbs/selectBBSMasterInfs.do")
    public String selectBBSMasterInfs(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, ModelMap model) throws Exception {
        boardMasterVO.setPageUnit(propertyService.getInt("pageUnit"));
        boardMasterVO.setPageSize(propertyService.getInt("pageSize"));

        PaginationInfo paginationInfo = new PaginationInfo();

        paginationInfo.setCurrentPageNo(boardMasterVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(boardMasterVO.getPageUnit());
        paginationInfo.setPageSize(boardMasterVO.getPageSize());

        boardMasterVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        boardMasterVO.setLastIndex(paginationInfo.getLastRecordIndex());
        boardMasterVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        Map<String, Object> map = egovBBSMasterService.selectBBSMasterInfs(boardMasterVO);
        int totCnt = Integer.parseInt((String) map.get("resultCnt"));

        paginationInfo.setTotalRecordCount(totCnt);

        model.addAttribute("resultList", map.get("resultList"));
        model.addAttribute("resultCnt", map.get("resultCnt"));
        model.addAttribute("paginationInfo", paginationInfo);

        return "egovframework/com/cop/bbs/EgovBBSMasterList";
    }

    @RequestMapping("/cop/bbs/selectBBSMasterDetail.do")
    public String selectBBSMasterDetail(@ModelAttribute("searchVO") BoardMasterVO searchVO, ModelMap model) throws Exception {
        BoardMasterVO vo = egovBBSMasterService.selectBBSMasterInf(searchVO);
        model.addAttribute("result", vo);

        return "egovframework/com/cop/bbs/EgovBBSMasterDetail";
    }

    @RequestMapping("/cop/bbs/updateBBSMasterView.do")
    public String updateBBSMasterView(@RequestParam("bbsId") String bbsId,
                                      @ModelAttribute("searchVO") BoardMaster searchVO, ModelMap model)
            throws Exception {


        BoardMasterVO boardMasterVO = new BoardMasterVO();


        //게시판유형코드
        ComDefaultCodeVO vo = new ComDefaultCodeVO();
        vo.setCodeId("COM101");
        List<?> codeResult = cmmUseService.selectCmmCodeDetail(vo);
        model.addAttribute("bbsTyCode", codeResult);

        // Primary Key 값 세팅
        boardMasterVO.setBbsId(bbsId);

        model.addAttribute("boardMasterVO", egovBBSMasterService.selectBBSMasterInf(boardMasterVO));

        return "egovframework/com/cop/bbs/EgovBBSMasterUpdt";
    }


    @RequestMapping("/cop/bbs/updateBBSMaster.do")
    public String updateBBSMaster(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, @ModelAttribute("boardMaster") BoardMaster boardMaster,
                                  BindingResult bindingResult, ModelMap model) throws Exception {

        //LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        //Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

        beanValidator.validate(boardMaster, bindingResult);
        if (bindingResult.hasErrors()) {
            BoardMasterVO vo = egovBBSMasterService.selectBBSMasterInf(boardMasterVO);

            model.addAttribute("result", vo);

            ComDefaultCodeVO comVo = new ComDefaultCodeVO();
            comVo.setCodeId("COM101");
            List<?> codeResult = cmmUseService.selectCmmCodeDetail(comVo);
            model.addAttribute("bbsTyCode", codeResult);

            return "egovframework/com/cop/bbs/EgovBBSMasterUpdt";
        }

        //if (isAuthenticated) {
            //boardMaster.setLastUpdusrId(user.getUniqId());
            boardMaster.setLastUpdusrId("313");
            egovBBSMasterService.updateBBSMasterInf(boardMaster);
        //}

        return "forward:/cop/bbs/selectBBSMasterInfs.do";
    }

    @RequestMapping("/cop/bbs/deleteBBSMaster.do")
    public String deleteBBSMaster(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, @ModelAttribute("boardMaster") BoardMaster boardMaster
    ) throws Exception {

        LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

        if (isAuthenticated) {
            boardMaster.setLastUpdusrId(user.getUniqId());
            egovBBSMasterService.deleteBBSMasterInf(boardMaster);
        }
        // status.setComplete();
        return "forward:/cop/bbs/selectBBSMasterInfs.do";
    }


}
