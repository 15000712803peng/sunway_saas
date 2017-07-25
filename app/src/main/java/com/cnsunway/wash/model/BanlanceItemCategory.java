package com.cnsunway.wash.model;

/**
 * Created by LL on 2016/3/25.
 */
public class BanlanceItemCategory extends BalanceItemParent{
        String  category;

        public String getCategory() {
                return category;
        }

        public void setCategory(String category) {
                this.category = category;
        }

        public BanlanceItemCategory(String category) {
                this.category = category;
        }
        public BanlanceItemCategory() {

        }
}
