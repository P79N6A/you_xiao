///*
//package com.runtoinfo.event.entity;
//
//*/
///**
// * Created by QiaoJunChao on 2018/8/22.
// *//*
//
//
//    public class TestClass extends RecyclerView.Adapter {
//
//        private Context context;
//
//        private List
//                brands;
//
//        private List
//                types;
//
//        private List
//                nums;
//
//        public Adapter(Activity context) {
//
//            this.context = context;
//
//            brands = new ArrayList<>();
//
//            types = new ArrayList<>();
//
//            nums = new ArrayList<>();
//
//            brands.add("");
//
//            types.add("");
//
//            nums.add("");
//
//        }
//
//        @Override
//
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//            View view = LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false);
//
//            ViewHolder holder = new ViewHolder(view);
//
//            return holder;
//
//        }
//
//        @Override
//
//        public void onBindViewHolder(final ViewHolder holder, final int position) {
//
//            if (holder.amountEt.getTag() instanceof TextWatcher) {
//
//                holder.amountEt.removeTextChangedListener(((TextWatcher) holder.amountEt.getTag()));
//
//            }
//
//            if (holder.typeEt.getTag() instanceof TextWatcher) {
//
//                holder.typeEt.removeTextChangedListener(((TextWatcher) holder.typeEt.getTag()));
//
//            }
//
//            if (holder.brandEt.getTag() instanceof TextWatcher) {
//
//                holder.brandEt.removeTextChangedListener(((TextWatcher) holder.brandEt.getTag()));
//
//            }
//
//            holder.amountEt.setText(nums.get(position));
//
//            holder.brandEt.setText(brands.get(position));
//
//            holder.typeEt.setText(types.get(position));
//
//            if (position == brands.size() –1){
//
//                holder.addTxt.setVisibility(View.VISIBLE);
//
//            }else{
//
//                holder.addTxt.setVisibility(View.GONE);
//
//            }
//
//            holder.deleteTv.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//
//                public void onClick(View v) {
//
//                    brands.remove(position);
//
//                    types.remove(position);
//
//                    nums.remove(position);
//
//                    notifyDataSetChanged();
//
//                    notifyItemRemoved(position);
//
//                }
//
//            });
//
//            holder.addTxt.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//
//                public void onClick(View v) {
//
//                    brands.add(position + 1, "");
//
//                    types.add(position + 1, "");
//
//                    nums.add(position + 1, "");
//
//                    notifyDataSetChanged();
//
//                    notifyItemRemoved(position + 1);
//
//                }
//
//            });
//
//            TextWatcher brandWatcher = new TextWatcher() {
//
//                @Override
//
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//
//                public void afterTextChanged(Editable s) {
//
//                    if (brands.get(position) != null) {
//
//                        brands.remove(position);
//
//                    }
//
//                    brands.add(position, s.toString());
//
//                }
//
//            };
//
//            holder.brandEt.addTextChangedListener(brandWatcher);
//
//            holder.brandEt.setTag(brandWatcher);
//
//            TextWatcher typeWatcher = new TextWatcher() {
//
//                @Override
//
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//
//                public void afterTextChanged(Editable s) {
//
//                    if (types.get(position) != null) {
//
//                        types.remove(position);
//
//                    }
//
//                    types.add(position, holder.typeEt.getText().toString());
//
//                }
//
//            };
//
//            holder.typeEt.addTextChangedListener(typeWatcher);
//
//            holder.typeEt.setTag(typeWatcher);
//
//            TextWatcher amountWatcher = new TextWatcher() {
//
//                @Override
//
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//
//                public void afterTextChanged(Editable s) {
//
//                    if (nums.get(position) != null) {
//
//                        nums.remove(position);
//
//                    }
//
//                    nums.add(position, holder.amountEt.getText().toString());
//
//                }
//
//            };
//
//            holder.amountEt.addTextChangedListener(amountWatcher);
//
//            holder.amountEt.setTag(amountWatcher);
//
//        }
//
//        @Override
//
//        public int getItemCount() {
//
//            return brands == null ? 0 : brands.size();
//
//        }
//
//        class ViewHolder extends RecyclerView.ViewHolder {
//
//            private EditText brandEt;
//
//            private EditText typeEt;
//
//            private EditText amountEt;
//
//            private LinearLayout llMain;
//
//            private TextView deleteTv, addTxt;
//
//            public ViewHolder(View itemView) {
//
//                super(itemView);
//
//                brandEt = (EditText) itemView.findViewById(R.id.item_recycler_equipment_brand_et);
//
//                typeEt = (EditText) itemView.findViewById(R.id.item_recycler_equipment_type_et);
//
//                amountEt = (EditText) itemView.findViewById(R.id.item_recycler_equipment_amount_et);
//
//                deleteTv = (TextView) itemView.findViewById(R.id.item_recycler_delete_tv);
//
//                addTxt = (TextView) itemView.findViewById(R.id.addEdd);
//
//                llMain = (LinearLayout) itemView.findViewById(R.id.item_recycler_ll_main);
//
//            }
//
//        }
//
//}
//*/
