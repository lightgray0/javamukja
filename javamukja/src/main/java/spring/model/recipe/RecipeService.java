package spring.model.recipe;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import spring.model.reply.ReplyInter;

@Service
public class RecipeService implements RecipeServiceInter{
	@Autowired
	private RecipeInter binter;

//	@Autowired
//	private ReplyInter rinter;

	public void delete(int bbsno) throws Exception{
		 
//        rinter.bdelete(bbsno);
        binter.delete(bbsno);
	}
	
	public void reply(RecipeDTO dto) throws Exception {
		Map map = new HashMap();
		map.put("grpno", dto.getGrpno());
		map.put("ansnum", dto.getAnsnum());
		
		binter.upAnsnum(map);
		binter.createReply(dto);
		
	}
}
