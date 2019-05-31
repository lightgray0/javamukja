package spring.model.recipe;

public interface RecipeServiceInter {
	void delete(int bbsno) throws Exception;

	void reply(RecipeDTO dto) throws Exception;
}
