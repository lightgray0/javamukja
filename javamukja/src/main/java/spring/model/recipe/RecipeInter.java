package spring.model.recipe;

import java.util.List;
import java.util.Map;

public interface RecipeInter {

	int create(RecipeDTO dto);

	RecipeDTO read(int bbsno);

	int update(RecipeDTO dto);

	int delete(int bbsno);

	List<RecipeDTO> list(Map map);

	int total(Map map);

	RecipeDTO readReply(int bbsno);

	void upAnsnum(Map map);

	int createReply(RecipeDTO dto);

	int passCheck(Map map);

	int refnumCheck(int bbsno);

	void upViewcnt(int bbsno);

}