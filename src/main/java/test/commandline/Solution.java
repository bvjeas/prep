package test.commandline;

import java.util.*;
import java.util.stream.Collectors;

enum Category {
    FAT, CARB, FIBER
}

class Item {
    public String value;
    public Category category;

    public Item(Category category, String value) {
        this.value = value;
        this.category = category;
    }
}

class Pantry {
    LinkedList<Item> items;
    HashMap<Category, List<Item>> hash;

    Pantry() {
        hash = new HashMap<>();
        items = new LinkedList<>();
    }

    public void add(Item item) {
        items.addLast(item);
        List<Item> hashItems = hash.get(item.category) == null ? new ArrayList<>() : hash.get(item.category);
        hashItems.add(item);
        hash.put(item.category, hashItems);
    }

    public void remove(Item item) {
        items.remove(item);
    }

    public static boolean hasEnough(HashMap<Category, List<Item>> hash) {
        List<List<Item>> counts = hash.values().stream().filter(x -> x.size() > 0).collect(Collectors.toList());
        int size = counts.size();
        return size >= 2 && counts.stream().map(List::size).mapToInt(Integer::intValue).sum() >= size + 1;
    }

    public void cook() {
        boolean selected = false;
        Iterator<Item> iterator = items.iterator();
        HashMap<Category, List<Item>> selectedItems = new HashMap<>();

        ArrayList<Category> order = new ArrayList<>();

        while(iterator.hasNext() && !selected) {
            Item item = iterator.next();
            List<Item> categorySelected = selectedItems.get(item.category);
            if(categorySelected == null) {
                order.add(item.category);
                categorySelected = new ArrayList<>();
            }
            categorySelected.add(item);
            selectedItems.put(item.category, categorySelected);
            selected = hasEnough(selectedItems);
        }

        ArrayList<Item> picked = new ArrayList<>();

        Iterator<Category> orderIterator = order.iterator();

        while (picked.size() < 3) {
            picked.addAll(selectItems(selectedItems.get(orderIterator.next()), 3 - picked.size()));
        }

        for (Item item : picked) {
            items.remove(item);
            hash.get(item.category).remove(item);
        }
    }

    private static ArrayList<Item> selectItems(List<Item> items, int required) {
        ArrayList<Item> selected = new ArrayList<>();
        while(selected.size() <= 2 && required > 0 && items.size() > 0) {
            selected.add(items.remove(0));
            required -= 1;
        }
        return selected;
    }
}


public class Solution {
    public static void main (String[] args) {
        Pantry ds = new Pantry();

        List<Item> items = new ArrayList<>();
        items.add(new Item(Category.CARB, "Beetroot"));
        items.add(new Item(Category.FIBER, "Carrot"));
        items.add(new Item(Category.FAT, "Olive"));
        items.add(new Item(Category.CARB, "Corn"));
        items.add(new Item(Category.CARB, "Potato"));
        items.add(new Item(Category.FIBER, "Broccoli"));
        items.add(new Item(Category.FAT, "Egg"));
        items.add(new Item(Category.FIBER, "Beans"));
        items.add(new Item(Category.FAT, "Cheese"));
        items.add(new Item(Category.CARB, "Rice"));
        items.add(new Item(Category.FIBER, "Spinach"));
        items.add(new Item(Category.FAT, "Oil"));

        items.forEach(item -> {
            ds.add(item);
            if(Pantry.hasEnough(ds.hash)) {
                ds.cook();
                System.out.print(1);
            } else {
                System.out.print(0);
            }
        });


    }
}
