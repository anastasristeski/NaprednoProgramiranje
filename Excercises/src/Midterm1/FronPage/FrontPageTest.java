package Midterm1.FronPage;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(String category) {
        super(String.format("Category %s was not found",category));
    }
}
class Category{
    String name;
    public Category(String name){
        this.name=name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }
}
abstract class NewsItem{
    String title;
    Date date;
    Category cat;

    public NewsItem(String title, Date date, Category cat) {
        this.title = title;
        this.date = date;
        this.cat = cat;
    }
    abstract String getCategory();
    abstract String getTeaser();
}
class TextNewsItem extends NewsItem{
    String text;
    public TextNewsItem(String title, Date date, Category cat,String text) {
        super(title, date, cat);
        this.text=text;
    }

    @Override
    String getCategory() {
        return cat.getName();
    }

    public String getTeaser() {
        Instant now = Instant.now();
        Duration duration = Duration.between(date.toInstant(), now);
        long minutesBetween = duration.toMinutes();
        String shortContent = text.length() > 80 ? text.substring(0,80) : text;
        return String.format("%s\n%d\n%s",title,minutesBetween,shortContent);
    }
}
class MediaNewsItem extends NewsItem{
    String url;
    int views;
    public MediaNewsItem(String title, Date date, Category cat,String content,int views) {
        super(title, date, cat);
        this.url =content;
        this.views=views;
    }

    @Override
    String getCategory() {
        return cat.getName();
    }

    public String getTeaser() {
        Instant now = Instant.now();
        Duration duration = Duration.between(date.toInstant(), now);
        long minutesBetween = duration.toMinutes();
        return String.format("%s\n%d\n%s\n%d",title,minutesBetween,url,views);
    }
}
class FrontPage{
    List<NewsItem> list;
    Category []categories;
    public FrontPage(Category[] categories) {
        this.categories = categories;
        list = new ArrayList<>();
    }
    public void addNewsItem(NewsItem newsItem){
        list.add(newsItem);
    }
    List<NewsItem> listByCategory(Category category){
        return list.stream()
                .filter(x->x.cat.equals(category))
                .collect(Collectors.toList());
    }

    List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        if(!checkCategory(category)){
            throw new CategoryNotFoundException(category);
        }
        else{
            return list.stream()
                    .filter(x->x.getCategory().equals(category))
                    .collect(Collectors.toList());
        }
    }
    public boolean checkCategory(String category){
        int flag = -1;
        for (Category c : categories){
            if(c.getName().equals(category)){
                flag+=1;
            }
        }
        return flag !=-1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(NewsItem ni : list){
            sb.append(ni.getTeaser()).append("\n");
        }
        return sb.toString();
    }
}
public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde