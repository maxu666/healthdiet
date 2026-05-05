<template>
  <div class="recipe-center">
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <!-- ================= 推荐食谱 ================= -->
      <el-tab-pane label="推荐食谱" name="recommend">
        <div class="tab-content">
          <el-tabs v-model="recommendSubTab" type="border-card" style="margin-bottom: 20px;">
            <el-tab-pane label="🔥 今日推荐" name="today"></el-tab-pane>
            <el-tab-pane label="🌱 健康推荐" name="health"></el-tab-pane>
            <el-tab-pane label="🥗 减脂推荐" name="weightLoss"></el-tab-pane>
            <el-tab-pane label="💪 增肌推荐" name="muscleGain"></el-tab-pane>
            <el-tab-pane label="👩‍⚕️ 营养师推荐" name="nutritionist">
              <div class="search-box">
                <el-input
                  v-model="nutritionistSearch"
                  placeholder="搜索营养师名称"
                  clearable
                  @keyup.enter="searchNutritionistRecipes"
                >
                  <template #append>
                    <el-button @click="searchNutritionistRecipes">
                      <el-icon><Search /></el-icon>
                    </el-button>
                  </template>
                </el-input>
              </div>
            </el-tab-pane>
          </el-tabs>

          <!-- 今日推荐 -->
          <div v-if="recommendSubTab === 'today'" class="recipe-grid">
            <div
              v-for="item in todayRecipes"
              :key="item.id"
              class="recipe-card"
            >
              <div class="cover"></div>
              <div class="recipe-title">{{ item.name }}</div>
              <div class="tags">
                <el-tag size="small" type="success">
                  {{ item.healthGoal || "健康" }}
                </el-tag>
                <el-tag size="small" type="info">
                  👨‍⚕️ {{ item.nickname || "匿名" }}
                </el-tag>
              </div>
              <div class="recipe-actions">
                <div class="recipe-rating">
                  <el-rate :model-value="ratingMap[item.id] || 0" disabled size="small" />
                  <span class="rating-text">{{ ratingMap[item.id] || 0.0 }}分</span>
                </div>
                <el-button link type="primary" @click="viewRecommendDetail(item.id)">
                  查看详情 →
                </el-button>
                <el-button link type="danger" @click="toggleFavourite(item.id)">
                  ❤️ 收藏
                </el-button>
              </div>
            </div>
            <div v-if="todayRecipes.length === 0" class="empty-state">
              <el-empty description="暂无今日推荐" />
            </div>
          </div>

          <!-- 健康推荐 -->
          <div v-else-if="recommendSubTab === 'health'" class="recipe-grid">
            <div
              v-for="item in healthRecipes"
              :key="item.id"
              class="recipe-card"
            >
              <div class="cover"></div>
              <div class="recipe-title">{{ item.name }}</div>
              <div class="tags">
                <el-tag size="small" type="success">
                  {{ item.healthGoal || "健康" }}
                </el-tag>
                <el-tag size="small" type="info">
                  👨‍⚕️ {{ item.nickname || "匿名" }}
                </el-tag>
              </div>
              <div class="recipe-actions">
                <div class="recipe-rating">
                  <el-rate :model-value="ratingMap[item.id] || 0" disabled size="small" />
                  <span class="rating-text">{{ ratingMap[item.id] || 0.0 }}分</span>
                </div>
                <el-button link type="primary" @click="viewRecommendDetail(item.id)">
                  查看详情 →
                </el-button>
                <el-button link type="danger" @click="toggleFavourite(item.id)">
                  ❤️ 收藏
                </el-button>
              </div>
            </div>
            <div v-if="healthRecipes.length === 0" class="empty-state">
              <el-empty description="暂无健康推荐" />
            </div>
          </div>

          <!-- 减脂推荐 -->
          <div v-else-if="recommendSubTab === 'weightLoss'" class="recipe-grid">
            <div
              v-for="item in weightLossRecipes"
              :key="item.id"
              class="recipe-card"
            >
              <div class="cover"></div>
              <div class="recipe-title">{{ item.name }}</div>
              <div class="tags">
                <el-tag size="small" type="success">
                  {{ item.healthGoal || "健康" }}
                </el-tag>
                <el-tag size="small" type="info">
                  👨‍⚕️ {{ item.nickname || "匿名" }}
                </el-tag>
              </div>
              <div class="recipe-actions">
                <div class="recipe-rating">
                  <el-rate :model-value="ratingMap[item.id] || 0" disabled size="small" />
                  <span class="rating-text">{{ ratingMap[item.id] || 0.0 }}分</span>
                </div>
                <el-button link type="primary" @click="viewRecommendDetail(item.id)">
                  查看详情 →
                </el-button>
                <el-button link type="danger" @click="toggleFavourite(item.id)">
                  ❤️ 收藏
                </el-button>
              </div>
            </div>
            <div v-if="weightLossRecipes.length === 0" class="empty-state">
              <el-empty description="暂无减脂推荐" />
            </div>
          </div>

          <!-- 增肌推荐 -->
          <div v-else-if="recommendSubTab === 'muscleGain'" class="recipe-grid">
            <div
              v-for="item in muscleGainRecipes"
              :key="item.id"
              class="recipe-card"
            >
              <div class="cover"></div>
              <div class="recipe-title">{{ item.name }}</div>
              <div class="tags">
                <el-tag size="small" type="success">
                  {{ item.healthGoal || "健康" }}
                </el-tag>
                <el-tag size="small" type="info">
                  👨‍⚕️ {{ item.nickname || "匿名" }}
                </el-tag>
              </div>
              <div class="recipe-actions">
                <div class="recipe-rating">
                  <el-rate :model-value="ratingMap[item.id] || 0" disabled size="small" />
                  <span class="rating-text">{{ ratingMap[item.id] || 0.0 }}分</span>
                </div>
                <el-button link type="primary" @click="viewRecommendDetail(item.id)">
                  查看详情 →
                </el-button>
                <el-button link type="danger" @click="toggleFavourite(item.id)">
                  ❤️ 收藏
                </el-button>
              </div>
            </div>
            <div v-if="muscleGainRecipes.length === 0" class="empty-state">
              <el-empty description="暂无增肌推荐" />
            </div>
          </div>

          <!-- 营养师推荐 -->
          <div v-else-if="recommendSubTab === 'nutritionist'" class="recipe-grid">
            <div
              v-for="item in nutritionistRecipes"
              :key="item.id"
              class="recipe-card"
            >
              <div class="cover"></div>
              <div class="recipe-title">{{ item.name }}</div>
              <div class="tags">
                <el-tag size="small" type="success">
                  {{ item.healthGoal || "健康" }}
                </el-tag>
                <el-tag size="small" type="info">
                  👨‍⚕️ {{ item.nickname || "匿名" }}
                </el-tag>
              </div>
              <div class="recipe-actions">
                <div class="recipe-rating">
                  <el-rate :model-value="ratingMap[item.id] || 0" disabled size="small" />
                  <span class="rating-text">{{ ratingMap[item.id] || 0.0 }}分</span>
                </div>
                <el-button link type="primary" @click="viewRecommendDetail(item.id)">
                  查看详情 →
                </el-button>
                <el-button link type="danger" @click="toggleFavourite(item.id)">
                  ❤️ 收藏
                </el-button>
              </div>
            </div>
            <div v-if="nutritionistRecipes.length === 0" class="empty-state">
              <el-empty description="暂无营养师推荐" />
            </div>
          </div>

        </div>
      </el-tab-pane>

      <!-- ================= 我的食谱 ================= -->
      <el-tab-pane label="我的食谱" name="mine">
        <!-- 工具栏 -->
        <div class="toolbar">
          <el-button type="primary" @click="startCreate">
            ＋ 新建食谱
          </el-button>
          <div class="breadcrumb" v-if="mineSubTab === 'detail'">
            <el-button link @click="mineSubTab = 'list'">
              ← 返回列表
            </el-button>
            <span> / </span>
            <span v-if="mineSubTab === 'list'">📋 食谱列表</span>
            <span v-else-if="mineSubTab === 'create'">＋ 新建食谱</span>
            <span v-else-if="mineSubTab === 'edit'">✏️ 编辑食谱</span>
            <span v-else-if="mineSubTab === 'detail'">📋 食谱详情</span>
          </div>
        </div>

        <!-- 列表视图 -->
        <div v-if="mineSubTab === 'list'">
          <div v-if="myList.length === 0" class="empty-state">
            <el-empty description="暂无食谱，点击上方按钮创建" />
          </div>
          <div v-else class="recipe-grid">
            <div
              v-for="item in myList"
              :key="item.id"
              class="recipe-card"
            >
              <div class="cover"></div>
              <div class="recipe-title">{{ item.name }}</div>
              <div class="tags">
                <el-tag size="small" type="success">
                  {{ item.healthGoal || "健康" }}
                </el-tag>
              </div>
              <div class="recipe-actions">
                <el-button link type="primary" @click="viewMyRecipeDetail(item.id)">
                  查看详情 →
                </el-button>
                <el-button link type="warning" @click="startEdit(item.id)">
                  ✏️ 编辑
                </el-button>
                <el-button link type="danger" @click="confirmDelete(item.id, item.name)">
                  🗑️ 删除
                </el-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 新建/编辑视图 -->
        <div v-else-if="mineSubTab === 'create' || mineSubTab === 'edit'" class="recipe-form">
          <el-form
            :model="form"
            :rules="rules"
            ref="formRef"
            label-width="100px"
          >
            <el-form-item label="食谱名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入食谱名称" />
            </el-form-item>

            <el-form-item label="目标">
              <el-select v-model="form.healthGoal" placeholder="请选择健康目标">
                <el-option
                  v-for="goal in healthGoals"
                  :key="goal.value"
                  :label="goal.label"
                  :value="goal.value"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="描述">
              <el-input
                v-model="form.description"
                type="textarea"
                placeholder="请输入食谱描述"
                :rows="3"
              />
            </el-form-item>

            <!-- 餐次选择 -->
            <el-form-item label="三餐安排">
              <div class="meals-row">
                <!-- 早餐 -->
                <div class="meal-card">
                  <div class="meal-header">
                    <div class="meal-header-left">
                      <span class="meal-icon">🌅</span>
                      <span class="meal-title">早餐</span>
                    </div>
                    <el-button type="primary" size="small" @click="openFoodDialog(0, 'food')">
                      ＋ 添加
                    </el-button>
                  </div>
                  <div class="meal-content">
                    <div
                      v-for="(item, index) in meals[0].items"
                      :key="index"
                      class="food-item"
                    >
                      <el-tag
                        :type="item.type === 'food' ? 'info' : 'warning'"
                        effect="dark"
                        size="small"
                      >
                        {{ item.type === 'food' ? '食材' : '菜品' }}
                      </el-tag>
                      <span class="food-name">{{ item.name }}</span>
                      <span class="food-weight">{{ item.weight }}g</span>
                      <el-button
                        type="danger"
                        size="small"
                        @click="removeItem(0, index)"
                      >
                        删除
                      </el-button>
                    </div>
                    <div v-if="meals[0].items.length === 0" class="empty-meal">
                      暂无食物
                    </div>
                  </div>
                </div>

                <!-- 午餐 -->
                <div class="meal-card">
                  <div class="meal-header">
                    <div class="meal-header-left">
                      <span class="meal-icon">☀️</span>
                      <span class="meal-title">午餐</span>
                    </div>
                    <el-button type="primary" size="small" @click="openFoodDialog(1, 'food')">
                      ＋ 添加
                    </el-button>
                  </div>
                  <div class="meal-content">
                    <div
                      v-for="(item, index) in meals[1].items"
                      :key="index"
                      class="food-item"
                    >
                      <el-tag
                        :type="item.type === 'food' ? 'info' : 'warning'"
                        effect="dark"
                        size="small"
                      >
                        {{ item.type === 'food' ? '食材' : '菜品' }}
                      </el-tag>
                      <span class="food-name">{{ item.name }}</span>
                      <span class="food-weight">{{ item.weight }}g</span>
                      <el-button
                        type="danger"
                        size="small"
                        @click="removeItem(1, index)"
                      >
                        删除
                      </el-button>
                    </div>
                    <div v-if="meals[1].items.length === 0" class="empty-meal">
                      暂无食物
                    </div>
                  </div>
                </div>

                <!-- 晚餐 -->
                <div class="meal-card">
                  <div class="meal-header">
                    <div class="meal-header-left">
                      <span class="meal-icon">🌙</span>
                      <span class="meal-title">晚餐</span>
                    </div>
                    <el-button type="primary" size="small" @click="openFoodDialog(2, 'food')">
                      ＋ 添加
                    </el-button>
                  </div>
                  <div class="meal-content">
                    <div
                      v-for="(item, index) in meals[2].items"
                      :key="index"
                      class="food-item"
                    >
                      <el-tag
                        :type="item.type === 'food' ? 'info' : 'warning'"
                        effect="dark"
                        size="small"
                      >
                        {{ item.type === 'food' ? '食材' : '菜品' }}
                      </el-tag>
                      <span class="food-name">{{ item.name }}</span>
                      <span class="food-weight">{{ item.weight }}g</span>
                      <el-button
                        type="danger"
                        size="small"
                        @click="removeItem(2, index)"
                      >
                        删除
                      </el-button>
                    </div>
                    <div v-if="meals[2].items.length === 0" class="empty-meal">
                      暂无食物
                    </div>
                  </div>
                </div>
              </div>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="submitForm">
                {{ mineSubTab === 'create' ? '发布' : '更新' }}
              </el-button>
              <el-button @click="cancelForm">
                取消
              </el-button>
            </el-form-item>
          </el-form>
        </div>

        <!-- 食谱详情视图 -->
        <div v-else-if="mineSubTab === 'detail'" class="recipe-detail">
          <div class="detail-header">
            <h2>{{ recipeDetail.name }}</h2>
            <p class="health-goal" v-if="recipeDetail.healthGoal">{{ recipeDetail.healthGoal }}</p>
          </div>

          <div class="detail-description" v-if="recipeDetail.description">
            <h3>描述</h3>
            <p>{{ recipeDetail.description }}</p>
          </div>

          <div class="nutrition-section">
            <h3>营养成分</h3>
            <el-row :gutter="10">
              <el-col :span="6">
                <div class="nutrition-item">
                  <div class="nutrition-label">热量</div>
                  <div class="nutrition-value">{{ totalNutrition.calorie }}kcal</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="nutrition-item">
                  <div class="nutrition-label">蛋白质</div>
                  <div class="nutrition-value">{{ totalNutrition.protein }}g</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="nutrition-item">
                  <div class="nutrition-label">碳水</div>
                  <div class="nutrition-value">{{ totalNutrition.carbohydrate }}g</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="nutrition-item">
                  <div class="nutrition-label">脂肪</div>
                  <div class="nutrition-value">{{ totalNutrition.fat }}g</div>
                </div>
              </el-col>
            </el-row>
          </div>

          <div class="meal-section" v-for="(meal, index) in mealItems" :key="index">
            <h3>{{ meal.type }}</h3>
            <el-table :data="meal.items" style="width: 100%">
              <el-table-column prop="name" label="名称" />
              <el-table-column prop="weight" label="重量(g)" width="100" />
              <el-table-column prop="type" label="类型" width="80">
                <template #default="scope">
                  <el-tag size="small" :type="scope.row.type === 'food' ? 'info' : 'warning'">
                    {{ scope.row.type === 'food' ? '食材' : '菜品' }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-tab-pane>

      <!-- ================= 收藏 ================= -->
      <el-tab-pane label="收藏食谱" name="favor">
        <div v-if="favouriteList.length === 0" class="empty-state">
          <el-empty description="暂未收藏" />
        </div>
        <div v-else class="recipe-grid">
          <div
            v-for="item in favouriteList"
            :key="item.name"
            class="recipe-card"
          >
            <div class="cover"></div>
            <div class="recipe-title">{{ item.name }}</div>
            <div class="tags">
              <el-tag size="small" type="success">
                {{ item.healthGoal || "健康" }}
              </el-tag>
              <el-tag size="small" type="info">
                👨‍⚕️ {{ item.nickname || "匿名" }}
              </el-tag>
            </div>
            <div class="recipe-actions">
              <el-button link type="primary" @click="viewRecommendDetail(item.id)">
                查看详情 →
              </el-button>
              <el-button link type="danger" @click="toggleFavourite(item.id)">
                🗑️ 取消收藏
              </el-button>
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 食材选择对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="selectedType === 'food' ? '选择食材' : '选择菜品'"
      width="800px"
      :append-to-body="true"
      class="food-dialog"
    >
      <!-- 搜索框和类型切换 -->
      <div class="dialog-header">
        <div class="type-selector-wrapper">
          <div
            class="type-tab"
            :class="{ active: selectedType === 'food' }"
            @click="selectedType = 'food'; onTypeChange()"
          >
            食材
          </div>
          <div
            class="type-tab"
            :class="{ active: selectedType === 'dish' }"
            @click="selectedType = 'dish'; onTypeChange()"
          >
            菜品
          </div>
        </div>
        <div class="search-wrapper">
          <el-input
            v-model="searchQuery"
            placeholder="搜索食物..."
            clearable
            class="search-input"
            @input="onSearchChange"
          >
            <template #prefix>🔍</template>
          </el-input>
        </div>
      </div>

      <div class="food-selector">
        <div v-if="selectedType === 'food'" class="category-sidebar">
          <div
            v-for="cat in categories"
            :key="cat"
            class="category-item"
            :class="{ active: activeCategory === cat }"
            @click="activeCategory = cat; onCategoryChange()"
          >
            {{ cat }}
          </div>
        </div>
        <div v-else class="category-sidebar">
          <div
            v-for="cat in dishCategories"
            :key="cat"
            class="category-item"
            :class="{ active: activeDishCategory === cat }"
            @click="activeDishCategory = cat; onCategoryChange()"
          >
            {{ cat }}
          </div>
        </div>

        <div class="food-content">
          <div class="food-grid">
            <div
              v-for="item in filteredList"
              :key="item.id"
              class="food-cell"
              :class="{ selected: selectedItemId === item.id }"
              @click="selectItem(item)"
            >
              <div v-if="selectedItemId === item.id" class="check-mark">✓</div>
              <img
                v-if="item.imageUrl"
                :src="baseURL + item.imageUrl"
                class="food-cell-img"
                loading="lazy"
                @load="handleImageLoad(item.id, $event)"
              />
              <div v-else class="food-cell-img food-cell-img-placeholder">暂无图片</div>
              <div class="food-cell-name">{{ item.name }}</div>
              <div class="food-cell-cal">
                {{ selectedType === 'food' ? (nutritionMap[item.id]?.calorie || '--') + 'kcal/100g' : (item.category || '--') }}
              </div>
            </div>
          </div>

          <div v-if="selectedItem" class="nutrition-info">
            <div class="nutrition-header">
              <div class="nutrition-title">{{ selectedItem.name }}
                <span v-if="selectedType === 'dish'"> [菜品]</span>
              </div>
              <div class="nutrition-close" @click="clearSelection">✕</div>
            </div>
            
            <div v-if="selectedType === 'food'" class="nutri-grid">
              <div class="nutri-card carbs">
                <div class="nutri-icon">🍞</div>
                <div class="nutri-label">碳水化合物</div>
                <div class="nutri-value">{{ nutrition.carbohydrate || 0 }}g</div>
              </div>
              <div class="nutri-card protein">
                <div class="nutri-icon">🥩</div>
                <div class="nutri-label">蛋白质</div>
                <div class="nutri-value">{{ nutrition.protein || 0 }}g</div>
              </div>
              <div class="nutri-card fat">
                <div class="nutri-icon">🥑</div>
                <div class="nutri-label">脂肪</div>
                <div class="nutri-value">{{ nutrition.fat || 0 }}g</div>
              </div>
            </div>
            
            <div class="weight-input-area">
              <span class="weight-label">{{ selectedType === 'food' ? '摄入重量' : '菜品重量' }}：</span>
              <el-input-number v-model="inputWeight" :min="1" :max="10000" />
              <span class="weight-unit">g</span>
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmSelection" class="confirm-btn">确定</el-button>
      </template>
    </el-dialog>

    <!-- 食谱详情对话框 -->
    <el-dialog v-model="recommendDetailVisible" title="食谱详情" width="800px">
      <div v-if="recommendDetail" class="recipe-detail">
        <div class="detail-header">
          <h2>{{ recommendDetail.name }}</h2>
          <p class="health-goal" v-if="recommendDetail.healthGoal">{{ recommendDetail.healthGoal }}</p>
        </div>

        <div class="detail-description" v-if="recommendDetail.description">
          <h3>描述</h3>
          <p>{{ recommendDetail.description }}</p>
        </div>

        <div class="nutrition-section">
          <h3>营养成分</h3>
          <el-row :gutter="10">
            <el-col :span="6">
              <div class="nutrition-item">
                <div class="nutrition-label">热量</div>
                <div class="nutrition-value">{{ recommendNutrition.calories }}kcal</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="nutrition-item">
                <div class="nutrition-label">蛋白质</div>
                <div class="nutrition-value">{{ recommendNutrition.protein }}g</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="nutrition-item">
                <div class="nutrition-label">碳水</div>
                <div class="nutrition-value">{{ recommendNutrition.carbs }}g</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="nutrition-item">
                <div class="nutrition-label">脂肪</div>
                <div class="nutrition-value">{{ recommendNutrition.fat }}g</div>
              </div>
            </el-col>
          </el-row>
        </div>

        <div class="meal-section" v-for="(meal, index) in recommendMealItems" :key="index">
          <h3>{{ meal.type }}</h3>
          <el-table :data="meal.items" style="width: 100%">
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="weight" label="重量(g)" width="100" />
            <el-table-column prop="type" label="类型" width="80">
              <template #default="scope">
                <el-tag size="small" :type="scope.row.type === 'food' ? 'info' : 'warning'">
                  {{ scope.row.type === 'food' ? '食材' : '菜品' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- ================= 评论模块 ================= -->
        <div class="review-section">
          <h3>💬 用户评价</h3>

          <!-- 发表评论 -->
          <div class="review-input">
            <el-rate v-model="reviewForm.rating" />
            <el-input
              v-model="reviewForm.content"
              type="textarea"
              placeholder="写下你的评价..."
              :rows="3"
            />
            <el-button type="primary" @click="submitReview">
              发表评论
            </el-button>
          </div>

          <!-- 评论列表 -->
          <div v-if="reviewList.length === 0" class="empty-state">
            <el-empty description="暂无评价" />
          </div>

          <div v-else>
            <div v-for="item in reviewList" :key="item.id" class="review-item">
              <div class="review-header">
                <span class="user">{{ item.username || '匿名用户' }}</span>
                <div class="review-header-right">
                  <el-rate :model-value="item.rating" disabled size="small" />
                  <el-button
                    v-if="item.userId === getUserId()"
                    type="text"
                    size="small"
                    class="delete-btn"
                    @click="deleteReview(item.id)"
                  >
                    🗑️ 删除
                  </el-button>
                </div>
              </div>
              <div class="review-content">{{ item.content }}</div>
              <div class="review-time">{{ item.createTime }}</div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Picture } from '@element-plus/icons-vue'
import request from '../utils/request'

// 标签页状态
const activeTab = ref('recommend')
const recommendSubTab = ref('today')
const mineSubTab = ref('list')

// 推荐食谱数据
const recipeList = ref([])
const myList = ref([])
const favouriteList = ref([])
const todayRecipes = ref([])
const healthRecipes = ref([])
const weightLossRecipes = ref([])
const muscleGainRecipes = ref([])
const nutritionistRecipes = ref([])
const nutritionistSearch = ref('')

// 评分映射
const ratingMap = ref({})

// 推荐食谱详情
const recommendDetailVisible = ref(false)
const recommendDetail = ref(null)

// 用户信息
const user = ref(JSON.parse(localStorage.getItem('user') || '{}'))

// 辅助函数
const getUserId = () => {
  return user.value?.id || null
}

const getUserRole = () => {
  return user.value?.role || 'USER'
}

// 评论数据
const reviewList = ref([])
const reviewForm = ref({
  recipeId: null,
  userId: getUserId(),
  rating: 5,
  content: ''
})

// 食材分类
const categories = ref(["主食", "肉类", "水产", "蛋奶类", "蔬菜", "水果", "坚果", "调料"])
const activeCategory = ref("主食")

// 菜品分类
const dishCategories = ref(["热菜", "凉菜", "汤", "小吃"])
const activeDishCategory = ref("热菜")

// 图片加载处理
const baseURL = "http://localhost:8080"
const imageLoading = ref(new Map())

const handleImageLoad = (foodId, event) => {
  imageLoading.value.set(foodId, true)
  if (event && event.target) {
    event.target.classList.add('loaded')
  }
}

// 我的食谱数据
const form = ref({
  name: "",
  healthGoal: "健康",
  description: "",
  userId: getUserId()
})

// 健康目标选项
const healthGoals = [
  { label: "健康", value: "健康" },
  { label: "减脂", value: "减脂" },
  { label: "增肌", value: "增肌" }
]

const rules = ref({
  name: [
    { required: true, message: '请输入食谱名称', trigger: 'blur' }
  ]
})

const formRef = ref(null)
const recipeDetail = ref({})
const meals = ref([
  { type: "早餐", items: [] },
  { type: "午餐", items: [] },
  { type: "晚餐", items: [] }
])

// 食材选择对话框
const dialogVisible = ref(false)
const selectedType = ref('food') // food 或 dish
const selectedMealIndex = ref(0)
const selectedItem = ref(null)
const selectedItemId = ref(null)
const inputWeight = ref(100)
const searchQuery = ref('')

// 计算属性
const currentList = computed(() => {
  if (selectedType.value === 'food') {
    return foodList.value.filter(item => item.category === activeCategory.value)
  } else {
    return dishList.value.filter(item => item.category === activeDishCategory.value)
  }
})

const filteredList = computed(() => {
  let list = currentList.value
  if (searchQuery.value) {
    list = list.filter(item => item.name.includes(searchQuery.value))
  }
  return list
})

const onSearchChange = () => {
  selectedItemId.value = null
  selectedItem.value = null
}

// 类型切换方法
const onTypeChange = () => {
  selectedItemId.value = null
  selectedItem.value = null
  nutrition.value = {}
  if (selectedType.value === 'food') {
    activeCategory.value = "主食"
  }
}

// 分类切换方法
const onCategoryChange = () => {
  selectedItemId.value = null
  selectedItem.value = null
  nutrition.value = {}
}

const foodList = ref([])
const dishList = ref([])
const nutrition = ref({})
const nutritionMap = ref({})
const loading = ref(false)

// 计算属性
const totalNutrition = computed(() => {
  if (!recipeDetail.value.items) {
    return { calorie: 0, protein: 0, carbohydrate: 0, fat: 0 }
  }
  
  let calorie = 0
  let protein = 0
  let carbohydrate = 0
  let fat = 0
  
  recipeDetail.value.items.forEach(item => {
    if (item.weight) {
      if (item.type === 'food' && item.foodId) {
        const food = foodList.value.find(f => f.id === item.foodId)
        if (food && nutritionMap.value[food.id]) {
          const nutri = nutritionMap.value[food.id]
          const ratio = item.weight / 100
          calorie += (nutri.calorie || 0) * ratio
          protein += (nutri.protein || 0) * ratio
          carbohydrate += (nutri.carbohydrate || 0) * ratio
          fat += (nutri.fat || 0) * ratio
        }
      } else if (item.type === 'dish' && item.dishId) {
        // 对于菜品，根据其食材列表计算营养成分
        const dishDetail = dishDetails.value[item.dishId]
        if (dishDetail && dishDetail.items) {
          // 计算菜品的原始总重量
          let originalTotalWeight = 0.0
          dishDetail.items.forEach(dishItem => {
            originalTotalWeight += dishItem.weight || 0
          })
          
          // 计算比例因子：用户输入的菜品重量 / 原始总重量
          const factor = originalTotalWeight > 0 ? item.weight / originalTotalWeight : 0.0
          
          dishDetail.items.forEach(dishItem => {
            if (dishItem.foodId && dishItem.weight) {
              const food = foodList.value.find(f => f.id === dishItem.foodId)
              if (food && nutritionMap.value[food.id]) {
                const nutri = nutritionMap.value[food.id]
                const dishItemRatio = dishItem.weight / 100
                const actualRatio = dishItemRatio * factor
                calorie += (nutri.calorie || 0) * actualRatio
                protein += (nutri.protein || 0) * actualRatio
                carbohydrate += (nutri.carbohydrate || 0) * actualRatio
                fat += (nutri.fat || 0) * actualRatio
              }
            }
          })
        }
      }
    }
  })
  
  return {
    calorie: Math.round(calorie),
    protein: Math.round(protein * 10) / 10,
    carbohydrate: Math.round(carbohydrate * 10) / 10,
    fat: Math.round(fat * 10) / 10
  }
})

const mealItems = computed(() => {
  if (!recipeDetail.value.items) {
    return [
      { type: "早餐", items: [] },
      { type: "午餐", items: [] },
      { type: "晚餐", items: [] }
    ]
  }
  
  const mealMap = {
    "早餐": [],
    "午餐": [],
    "晚餐": []
  }
  
  recipeDetail.value.items.forEach(item => {
    if (mealMap[item.mealType]) {
      // 添加名称属性
      const itemWithName = {
        ...item,
        name: item.type === 'food' 
          ? foodList.value.find(f => f.id === item.foodId)?.name || '未知食材'
          : dishList.value.find(d => d.id === item.dishId)?.name || '未知菜品'
      }
      mealMap[item.mealType].push(itemWithName)
    }
  })
  
  return [
    { type: "早餐", items: mealMap["早餐"] || [] },
    { type: "午餐", items: mealMap["午餐"] || [] },
    { type: "晚餐", items: mealMap["晚餐"] || [] }
  ]
})

const recommendNutrition = computed(() => {
  if (!recommendDetail.value?.items) {
    return { calories: 0, protein: 0, carbs: 0, fat: 0 }
  }

  let calories = 0
  let protein = 0
  let carbs = 0
  let fat = 0

  recommendDetail.value.items.forEach(item => {
    if (item.weight) {
      if (item.type === 'food' && item.foodId) {
        const food = foodList.value.find(f => f.id === item.foodId)
        if (food && nutritionMap.value[food.id]) {
          const nutri = nutritionMap.value[food.id]
          const ratio = item.weight / 100
          calories += (nutri.calorie || 0) * ratio
          protein += (nutri.protein || 0) * ratio
          carbs += (nutri.carbohydrate || 0) * ratio
          fat += (nutri.fat || 0) * ratio
        }
      } else if (item.type === 'dish' && item.dishId) {
        // 对于菜品，根据其食材列表计算营养成分
        const dishDetail = dishDetails.value[item.dishId]
        if (dishDetail && dishDetail.items) {
          // 计算菜品的原始总重量
          let originalTotalWeight = 0.0
          dishDetail.items.forEach(dishItem => {
            originalTotalWeight += dishItem.weight || 0
          })
          
          // 计算比例因子：用户输入的菜品重量 / 原始总重量
          const factor = originalTotalWeight > 0 ? item.weight / originalTotalWeight : 0.0
          
          dishDetail.items.forEach(dishItem => {
            if (dishItem.foodId && dishItem.weight) {
              const food = foodList.value.find(f => f.id === dishItem.foodId)
              if (food && nutritionMap.value[food.id]) {
                const nutri = nutritionMap.value[food.id]
                const dishItemRatio = dishItem.weight / 100
                const actualRatio = dishItemRatio * factor
                calories += (nutri.calorie || 0) * actualRatio
                protein += (nutri.protein || 0) * actualRatio
                carbs += (nutri.carbohydrate || 0) * actualRatio
                fat += (nutri.fat || 0) * actualRatio
              }
            }
          })
        }
      }
    }
  })

  return {
    calories: Math.round(calories),
    protein: Math.round(protein * 10) / 10,
    carbs: Math.round(carbs * 10) / 10,
    fat: Math.round(fat * 10) / 10
  }
})

const recommendMealItems = computed(() => {
  if (!recommendDetail.value?.items) {
    return [
      { type: "早餐", items: [] },
      { type: "午餐", items: [] },
      { type: "晚餐", items: [] }
    ]
  }

  const mealMap = {
    "早餐": [],
    "午餐": [],
    "晚餐": []
  }

  recommendDetail.value.items.forEach(item => {
    if (mealMap[item.mealType]) {
      // 添加名称属性
      const itemWithName = {
        ...item,
        name: item.type === 'food' 
          ? foodList.value.find(f => f.id === item.foodId)?.name || '未知食材'
          : dishList.value.find(d => d.id === item.dishId)?.name || '未知菜品'
      }
      mealMap[item.mealType].push(itemWithName)
    }
  })

  return [
    { type: "早餐", items: mealMap["早餐"] || [] },
    { type: "午餐", items: mealMap["午餐"] || [] },
    { type: "晚餐", items: mealMap["晚餐"] || [] }
  ]
})

// 方法
const handleTabClick = (tab) => {
  if (tab.props.name === 'mine') {
    mineSubTab.value = 'list'
  }
}

// 菜品详情映射，用于缓存菜品详情
const dishDetails = ref({})

const loadFoodData = async () => {
  try {
    const [foodRes, dishRes] = await Promise.all([
      request.get('/food/listFull'),
      request.get('/dish/listByCreator', { params: { creatorId: getUserId() } })
    ])
    // 转换listFull返回的嵌套结构为扁平结构
    foodList.value = foodRes.map(item => ({
      ...item.food,
      calorie: item.nutrition?.calorie,
      protein: item.nutrition?.protein,
      fat: item.nutrition?.fat,
      carbohydrate: item.nutrition?.carbohydrate
    }))
    // 同时更新nutritionMap
    nutritionMap.value = {}
    foodRes.forEach(item => {
      if (item.food && item.nutrition) {
        nutritionMap.value[item.food.id] = item.nutrition
      }
    })
    dishList.value = dishRes
  } catch (e) {
    console.error('加载食材数据失败', e)
  }
}

// 获取菜品详情
const getDishDetail = async (dishId) => {
  if (dishDetails.value[dishId]) {
    return dishDetails.value[dishId]
  }
  
  try {
    const res = await request.get('/dish/detail', { params: { id: dishId } })
    dishDetails.value[dishId] = res
    return res
  } catch (e) {
    console.error('获取菜品详情失败', e)
    return null
  }
}

const loadTodayRecommend = async () => {
  try {
    const res = await request.get("/recipe/recommend/today")
    todayRecipes.value = res
    await loadRecipeRatings(res.map(r => r.id))
  } catch (e) {
    console.error("获取今日推荐失败", e)
    //  fallback to general recommend if specific endpoint fails
    try {
      const res = await request.get("/recipe/recommend")
      todayRecipes.value = res.slice(0, 6)
      await loadRecipeRatings(todayRecipes.value.map(r => r.id))
    } catch (fallbackError) {
      console.error("获取推荐食谱失败", fallbackError)
      todayRecipes.value = []
    }
  }
}

// 批量加载食谱评分
const loadRecipeRatings = async (recipeIds) => {
  for (const id of recipeIds) {
    if (!ratingMap.value[id]) {
      const rating = await getAverageRating(id)
      ratingMap.value[id] = rating
    }
  }
}

const loadHealthRecommend = async () => {
  try {
    const res = await request.get("/recipe/recommend/health")
    healthRecipes.value = res
    await loadRecipeRatings(res.map(r => r.id))
  } catch (e) {
    console.error("获取健康推荐失败", e)
    //  fallback to general recommend if specific endpoint fails
    try {
      const res = await request.get("/recipe/recommend")
      healthRecipes.value = res.filter(recipe => recipe.healthGoal === "健康").slice(0, 6)
      await loadRecipeRatings(healthRecipes.value.map(r => r.id))
    } catch (fallbackError) {
      console.error("获取推荐食谱失败", fallbackError)
      healthRecipes.value = []
    }
  }
}

const loadWeightLossRecommend = async () => {
  try {
    const res = await request.get("/recipe/recommend/weightLoss")
    weightLossRecipes.value = res
    await loadRecipeRatings(res.map(r => r.id))
  } catch (e) {
    console.error("获取减脂推荐失败", e)
    //  fallback to general recommend if specific endpoint fails
    try {
      const res = await request.get("/recipe/recommend")
      weightLossRecipes.value = res.filter(recipe => recipe.healthGoal === "减脂").slice(0, 6)
      await loadRecipeRatings(weightLossRecipes.value.map(r => r.id))
    } catch (fallbackError) {
      console.error("获取推荐食谱失败", fallbackError)
      weightLossRecipes.value = []
    }
  }
}

const loadMuscleGainRecommend = async () => {
  try {
    const res = await request.get("/recipe/recommend/muscleGain")
    muscleGainRecipes.value = res
    await loadRecipeRatings(res.map(r => r.id))
  } catch (e) {
    console.error("获取增肌推荐失败", e)
    //  fallback to general recommend if specific endpoint fails
    try {
      const res = await request.get("/recipe/recommend")
      muscleGainRecipes.value = res.filter(recipe => recipe.healthGoal === "增肌").slice(0, 6)
      await loadRecipeRatings(muscleGainRecipes.value.map(r => r.id))
    } catch (fallbackError) {
      console.error("获取推荐食谱失败", fallbackError)
      muscleGainRecipes.value = []
    }
  }
}

const loadNutritionistRecommend = async () => {
  try {
    const res = await request.get("/recipe/recommend/nutritionist")
    nutritionistRecipes.value = res
    await loadRecipeRatings(res.map(r => r.id))
  } catch (e) {
    console.error("获取营养师推荐失败", e)
    //  fallback to general recommend if specific endpoint fails
    try {
      const res = await request.get("/recipe/recommend")
      nutritionistRecipes.value = res.filter(recipe => recipe.creatorType === "NUTRITIONIST").slice(0, 6)
      await loadRecipeRatings(nutritionistRecipes.value.map(r => r.id))
    } catch (fallbackError) {
      console.error("获取推荐食谱失败", fallbackError)
      nutritionistRecipes.value = []
    }
  }
}

const searchNutritionistRecipes = async () => {
  try {
    if (!nutritionistSearch.value) {
      await loadNutritionistRecommend()
      return
    }
    const res = await request.get("/recipe/search/nutritionist", {
      params: { name: nutritionistSearch.value }
    })
    nutritionistRecipes.value = res
  } catch (e) {
    console.error("搜索营养师推荐失败", e)
    ElMessage.error("搜索失败")
  }
}

const loadMine = async () => {
  try {
    const res = await request.get("/recipe/list", {
      params: { userId: getUserId() }
    })
    myList.value = res
  } catch (e) {
    console.error("获取我的食谱失败", e)
  }
}

const loadFavourites = async () => {
  try {
    const res = await request.get("/favourite/list", {
      params: { userId: getUserId() }
    })
    // res 是食谱ID列表，需要转换为食谱对象列表
    const recipeIds = res
    const recipes = []
    for (const id of recipeIds) {
      try {
        const recipe = await request.get(`/recipe/${id}`)
        // 需要获取完整的食谱信息，这里需要从其他地方获取
        // 先从已加载的食谱列表中查找
        const found = [...recipeList.value, ...todayRecipes.value, ...healthRecipes.value, ...weightLossRecipes.value, ...muscleGainRecipes.value, ...nutritionistRecipes.value].find(r => r.id === id)
        if (found) {
          recipes.push(found)
        }
      } catch (e) {
        console.error(`获取食谱 ${id} 失败`, e)
      }
    }
    favouriteList.value = recipes
  } catch (e) {
    console.error("获取收藏失败", e)
  }
}

const toggleFavourite = async (recipeId) => {
  try {
    // 先检查是否已收藏
    const isFavorited = await request.get("/favourite/check", {
      params: { userId: getUserId(), recipeId }
    })
    
    if (isFavorited) {
      // 取消收藏
      await request.delete("/favourite/remove", {
        params: { userId: getUserId(), recipeId }
      })
      ElMessage.success("取消收藏成功")
    } else {
      // 添加收藏
      await request.post("/favourite/add", null, {
        params: { userId: getUserId(), recipeId }
      })
      ElMessage.success("收藏成功")
    }
    await loadFavourites()
  } catch (e) {
    console.error("操作失败", e)
    ElMessage.error("操作失败")
  }
}

const viewRecommendDetail = async (id) => {
  try {
    const res = await request.get(`/recipe/${id}`)
    
    // 获取所有相关菜品的详情
    if (res.items) {
      const dishIds = res.items
        .filter(item => item.type === 'dish' && item.dishId)
        .map(item => item.dishId)
      
      // 去重
      const uniqueDishIds = [...new Set(dishIds)]
      
      // 并行获取所有菜品详情
      await Promise.all(
        uniqueDishIds.map(dishId => getDishDetail(dishId))
      )
    }
    
    recommendDetail.value = res
    recommendDetailVisible.value = true
    
    // 加载评论
    await loadReviews(id)
  } catch (e) {
    console.error("获取食谱详情失败", e)
    ElMessage.error("获取食谱详情失败")
  }
}

// 加载评论
const loadReviews = async (recipeId) => {
  try {
    const res = await request.get('/recipereview/list', {
      params: { recipeId }
    })
    reviewList.value = res
  } catch (e) {
    console.error("加载评论失败", e)
  }
}

// 提交评论
const submitReview = async () => {
  if (!reviewForm.value.content) {
    ElMessage.warning("请输入评价内容")
    return
  }

  try {
    reviewForm.value.recipeId = recommendDetail.value.id

    await request.post('/recipereview/add', reviewForm.value)

    ElMessage.success("评价成功")

    reviewForm.value.content = ''
    reviewForm.value.rating = 5

    await loadReviews(recommendDetail.value.id)
  } catch (e) {
    console.error("评价失败", e)
    ElMessage.error("评价失败")
  }
}

// 删除评论
const deleteReview = async (reviewId) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这条评论吗？',
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await request.delete('/recipereview/delete', {
      params: {
        reviewId: reviewId,
        userId: getUserId()
      }
    })

    ElMessage.success("删除成功")
    await loadReviews(recommendDetail.value.id)
  } catch (e) {
    if (e !== 'cancel') {
      console.error("删除失败", e)
      ElMessage.error("删除失败")
    }
  }
}

// 获取食谱平均评分
const getAverageRating = async (recipeId) => {
  try {
    const res = await request.get('/recipereview/averageRating', {
      params: { recipeId }
    })
    return res
  } catch (e) {
    console.error("获取评分失败", e)
    return 0
  }
}

// 获取评论数量
const getReviewCount = async (recipeId) => {
  try {
    const res = await request.get('/recipereview/count', {
      params: { recipeId }
    })
    return res
  } catch (e) {
    console.error("获取评论数量失败", e)
    return 0
  }
}

const viewMyRecipeDetail = async (id) => {
  try {
    const res = await request.get(`/recipe/${id}`)
    
    // 获取所有相关菜品的详情
    if (res.items) {
      const dishIds = res.items
        .filter(item => item.type === 'dish' && item.dishId)
        .map(item => item.dishId)
      
      // 去重
      const uniqueDishIds = [...new Set(dishIds)]
      
      // 并行获取所有菜品详情
      await Promise.all(
        uniqueDishIds.map(dishId => getDishDetail(dishId))
      )
    }
    
    recipeDetail.value = res
    mineSubTab.value = 'detail'
  } catch (e) {
    console.error("获取食谱详情失败", e)
    ElMessage.error("获取食谱详情失败")
  }
}

const startCreate = () => {
  form.value = {
    name: "",
    healthGoal: "健康",
    description: "",
    userId: getUserId()
  }
  meals.value = [
    { type: "早餐", items: [] },
    { type: "午餐", items: [] },
    { type: "晚餐", items: [] }
  ]
  mineSubTab.value = 'create'
}

const startEdit = async (id) => {
  try {
    const res = await request.get(`/recipe/${id}`)
    form.value = {
      name: res.name,
      healthGoal: res.healthGoal || "健康",
      description: res.description,
      userId: getUserId()
    }
    
    meals.value = [
      { type: "早餐", items: [] },
      { type: "午餐", items: [] },
      { type: "晚餐", items: [] }
    ]
    
    if (res.items) {
      res.items.forEach(item => {
        const mealIndex = item.mealType === "早餐" ? 0 : item.mealType === "午餐" ? 1 : 2
        meals.value[mealIndex].items.push({
          ...item,
          name: item.type === 'food' 
            ? foodList.value.find(f => f.id === item.foodId)?.name || '未知'
            : dishList.value.find(d => d.id === item.dishId)?.name || '未知'
        })
      })
    }
    
    recipeDetail.value = res
    mineSubTab.value = 'edit'
  } catch (e) {
    console.error("获取食谱详情失败", e)
    ElMessage.error("获取食谱详情失败")
  }
}

const confirmDelete = async (id, name) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除食谱「${name}」吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await request.delete(`/recipe/delete/${id}`)
    ElMessage.success("删除成功")
    await loadMine()
  } catch (e) {
    if (e !== 'cancel') {
      console.error("删除失败", e)
      ElMessage.error("删除失败")
    }
  }
}

const submitForm = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const items = []
        
        meals.value.forEach((meal, mealIndex) => {
          meal.items.forEach(item => {
            items.push({
              foodId: item.type === 'food' ? item.foodId : null,
              dishId: item.type === 'dish' ? item.dishId : null,
              type: item.type,
              mealType: meal.type,
              weight: item.weight
            })
          })
        })
        
        const dto = {
          name: form.value.name,
          healthGoal: form.value.healthGoal,
          description: form.value.description,
          userId: form.value.userId,
          creatorType: getUserRole(),
          items
        }
        
        // 调试：打印提交的数据
        console.log('提交的数据:', JSON.stringify(dto, null, 2))
        console.log('mineSubTab:', mineSubTab.value)
        console.log('recipeDetail:', recipeDetail.value)
        
        if (mineSubTab.value === 'create') {
          await request.post('/recipe/add', dto)
          ElMessage.success("发布成功")
        } else {
          const recipeId = recipeDetail.value?.id
          console.log('更新的recipeId:', recipeId)
          await request.put(`/recipe/update/${recipeId}`, dto)
          ElMessage.success("更新成功")
        }
        
        mineSubTab.value = 'list'
        await loadMine()
      } catch (e) {
        console.error("发布失败", e)
        ElMessage.error("发布失败")
      }
    }
  })
}

const cancelForm = () => {
  mineSubTab.value = 'list'
}

const openFoodDialog = (mealIndex, type) => {
  selectedMealIndex.value = mealIndex
  selectedType.value = type
  selectedItem.value = null
  selectedItemId.value = null
  inputWeight.value = 100
  dialogVisible.value = true
}



const selectItem = (item) => {
  selectedItemId.value = item.id
  selectedItem.value = item
  inputWeight.value = 100
  
  if (selectedType.value === 'food') {
    loadNutrition(item.id)
  }
}

const clearSelection = () => {
  selectedItemId.value = null
  selectedItem.value = null
  nutrition.value = {}
  inputWeight.value = 100
}

const loadNutrition = async (foodId) => {
  try {
    const res = await request.get(`/nutrition/${foodId}`)
    nutrition.value = res
  } catch (e) {
    console.error(e)
  }
}

const confirmSelection = () => {
  if (!selectedItem.value) return
  
  const item = {
    foodId: selectedType.value === 'food' ? selectedItem.value.id : null,
    dishId: selectedType.value === 'dish' ? selectedItem.value.id : null,
    type: selectedType.value,
    name: selectedItem.value.name,
    weight: inputWeight.value
  }
  
  meals.value[selectedMealIndex.value].items.push(item)
  dialogVisible.value = false
}

const removeItem = (mealIndex, itemIndex) => {
  meals.value[mealIndex].items.splice(itemIndex, 1)
}

onMounted(async () => {
  await loadFoodData()
  loadTodayRecommend()
  loadHealthRecommend()
  loadWeightLossRecommend()
  loadMuscleGainRecommend()
  loadNutritionistRecommend()
  loadMine()
  loadFavourites()
})
</script>

<style scoped>
.recipe-center {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.tab-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.recipe-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.recipe-card {
  background: white;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;
}

.recipe-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

.cover {
  height: 110px;
  border-radius: 10px;
  margin-bottom: 10px;
  background-image: url('/recipe_image.jpg');
  background-size: cover;
  background-position: center;
}



.recipe-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
}

.tags {
  margin-bottom: 12px;
}

.recipe-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}

.recipe-rating {
  display: flex;
  align-items: center;
  gap: 4px;
  width: 100%;
}

.rating-text {
  font-size: 14px;
  color: #f5a623;
  font-weight: bold;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: #f9f9f9;
  border-radius: 8px;
  margin-top: 20px;
}

/* 食物选择对话框样式 */
.food-dialog :deep(.el-dialog__header) {
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  margin: 0;
  padding: 20px;
  border-radius: 16px 16px 0 0;
}

.food-dialog :deep(.el-dialog__title) {
  color: white;
  font-weight: 700;
  font-size: 18px;
}

.food-dialog :deep(.el-dialog__close) {
  color: white;
}

.food-dialog :deep(.el-dialog__body) {
  padding: 24px;
  background: #f9fafb;
}

.food-dialog :deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
}

.dialog-header {
  margin-bottom: 16px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e8e8e8;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
}

.type-selector-wrapper {
  display: flex;
  gap: 8px;
  background: #f3f4f6;
  padding: 4px;
  border-radius: 24px;
}

.type-tab {
  padding: 8px 20px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
  color: #666;
}

.type-tab:hover {
  background: rgba(255, 255, 255, 0.5);
}

.type-tab.active {
  background: #fff;
  color: #f97316;
  box-shadow: 0 2px 8px rgba(249, 115, 22, 0.2);
}

.search-wrapper {
  flex: 1;
  max-width: 250px;
}

.search-input {
  width: 100%;
}

.food-selector {
  display: flex;
  gap: 0;
  height: 420px;
  background: #f9fafb;
  border-radius: 12px;
  overflow: hidden;
}

.category-sidebar {
  width: 100px;
  background: transparent;
  padding: 12px 8px;
  flex-shrink: 0;
}

.category-item {
  padding: 10px 8px;
  text-align: center;
  border-radius: 20px;
  cursor: pointer;
  font-size: 13px;
  color: #666;
  margin-bottom: 6px;
  transition: all 0.2s;
}

.category-item:hover {
  background: rgba(249, 115, 22, 0.08);
  color: #c2410c;
}

.category-item.active {
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  color: #fff;
  box-shadow: 0 4px 12px rgba(249, 115, 22, 0.3);
}

.food-content {
  flex: 1;
  padding: 12px 15px;
  background: #fff;
  border-radius: 12px;
  margin: 8px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.food-content .food-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  flex: 1;
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: #d1d5db transparent;
}

.food-content .food-grid::-webkit-scrollbar {
  width: 6px;
}

.food-content .food-grid::-webkit-scrollbar-thumb {
  background: #d1d5db;
  border-radius: 3px;
}

.food-cell {
  padding: 12px 8px;
  border-radius: 12px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: relative;
}

.food-cell:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}

.food-cell.selected {
  background: linear-gradient(135deg, #ffedd5 0%, #ffd5b5 100%);
  box-shadow: 0 4px 16px rgba(249, 115, 22, 0.25);
}

.food-cell.selected .food-cell-name {
  color: #9a3412;
}

.food-cell.selected .food-cell-cal {
  color: #c2410c;
}

.check-mark {
  position: absolute;
  top: 6px;
  right: 6px;
  width: 24px;
  height: 24px;
  background: #f97316;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 14px;
  font-weight: bold;
  box-shadow: 0 2px 8px rgba(249, 115, 22, 0.3);
}

.food-cell-name {
  font-size: 13px;
  font-weight: 600;
  margin-bottom: 4px;
  color: #333;
}

.food-cell-cal {
  font-size: 11px;
  color: #999;
}

.food-cell-img {
  width: 65px;
  height: 65px;
  object-fit: cover;
  border-radius: 10px;
  margin: 0 auto 8px;
  display: block;
  transition: opacity 0.3s ease;
  opacity: 0;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
}

.food-cell-img.loaded {
  opacity: 1;
}

.food-cell-img-placeholder {
  background: linear-gradient(135deg, #f3f4f6 0%, #e5e7eb 100%);
  color: #9ca3af;
  font-size: 11px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 65px;
  height: 65px;
  border-radius: 10px;
  margin: 0 auto 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.food-cell.selected .food-cell-img-placeholder {
  background: linear-gradient(135deg, #fed7aa 0%, #fdba74 100%);
  color: #9a3412;
}

.nutrition-info {
  margin-top: 15px;
  padding: 18px;
  background: linear-gradient(135deg, #fffaf5 0%, #fff7ed 100%);
  border-radius: 12px;
  flex-shrink: 0;
  border: 1px solid #fed7aa;
  box-shadow: 0 2px 8px rgba(249, 115, 22, 0.06);
}

.nutrition-title {
  font-size: 15px;
  font-weight: 700;
  margin-bottom: 0;
  color: #431407;
}

.nutrition-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.nutrition-close {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: #fef3c7;
  color: #92400e;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.nutrition-close:hover {
  background: #f97316;
  color: #fff;
}

.nutri-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  margin-bottom: 15px;
}

.nutri-card {
  background: white;
  padding: 14px 10px;
  border-radius: 10px;
  text-align: center;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.04);
  transition: all 0.2s;
}

.nutri-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.nutri-icon {
  font-size: 24px;
  margin-bottom: 6px;
}

.nutri-label {
  font-size: 12px;
  color: #78350f;
  margin-bottom: 4px;
}

.nutri-value {
  font-size: 18px;
  font-weight: 800;
  color: #f97316;
}

.weight-input-area {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 0;
  padding: 14px;
  background: linear-gradient(135deg, #fffef5 0%, #fef8e8 100%);
  border-radius: 10px;
  border: 1px solid #f0e9d8;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.04);
}

.weight-label {
  font-size: 14px;
  color: #7a6b5a;
  font-weight: 600;
}

.weight-unit {
  font-size: 14px;
  color: #8b7355;
  font-weight: 600;
}

.confirm-btn {
  padding: 10px 28px;
  border-radius: 8px;
  font-size: 15px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
}

.recipe-form {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.1);
  margin-top: 20px;
}

.meals-row {
  display: flex;
  gap: 20px;
  justify-content: space-between;
}

.meal-card {
  flex: 1;
  background: #f9f9f9;
  border: 1px solid #e4e7ed;
  border-radius: 12px;
  padding: 20px;
  transition: all 0.3s ease;
  min-width: 280px;
}

.meal-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-color: #409EFF;
}

.meal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e4e7ed;
}

.meal-header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.meal-icon {
  font-size: 20px;
}

.meal-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.meal-container {
  border: 1px solid #e4e7ed;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 20px;
  transition: all 0.3s ease;
  background: #f9f9f9;
}

.meal-container:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  border-color: #409EFF;
}

.meal-content {
  margin-bottom: 16px;
}

.food-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 8px;
  border-bottom: 1px solid #f0f0f0;
  transition: all 0.2s ease;
  flex-wrap: wrap;
}

.food-item:hover {
  background: rgba(64, 158, 255, 0.05);
  border-radius: 6px;
}

.food-item:last-child {
  border-bottom: none;
}

.food-name {
  flex: 1;
  font-weight: 500;
  font-size: 13px;
  color: #333;
  min-width: 80px;
}

.food-weight {
  font-size: 12px;
  color: #666;
  background: #ecf5ff;
  padding: 2px 6px;
  border-radius: 10px;
  white-space: nowrap;
}

.empty-meal {
  text-align: center;
  color: #999;
  padding: 30px 0;
  font-size: 14px;
}

.meal-actions {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}

.meal-actions .el-button {
  border-radius: 20px;
  padding: 8px 24px;
  font-size: 14px;
  transition: all 0.3s ease;
}

.meal-actions .el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.recipe-form .el-form-item {
  margin-bottom: 20px;
}

.recipe-form .el-form-item__label {
  font-weight: 600;
  color: #333;
  font-size: 14px;
}

.recipe-form .el-input,
.recipe-form .el-select,
.recipe-form .el-textarea {
  border-radius: 8px;
  border: 1px solid #dcdfe6;
  transition: all 0.3s ease;
}

.recipe-form .el-input:focus-within,
.recipe-form .el-select:focus-within,
.recipe-form .el-textarea:focus-within {
  border-color: #409EFF;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.recipe-form .el-button--primary {
  border-radius: 8px;
  padding: 10px 24px;
  font-size: 14px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.recipe-form .el-button--primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.recipe-form .el-button {
  border-radius: 8px;
  transition: all 0.3s ease;
}

.recipe-form .el-button:hover {
  transform: translateY(-1px);
}

.empty-meal {
  text-align: center;
  color: #999;
  padding: 20px 0;
}

.meal-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

/* 食物选择对话框样式 */
.dialog-header {
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e8e8e8;
}

.food-selector {
  display: flex;
  gap: 0;
  height: 420px;
}

.category-sidebar {
  width: 100px;
  background: #f5f5f5;
  border-radius: 8px;
  padding: 8px;
  flex-shrink: 0;
}

.category-item {
  padding: 10px 8px;
  text-align: center;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 13px;
}

.category-item:hover {
  background: #e6e6e6;
}

.category-item.active {
  background: #409EFF;
  color: #fff;
}

.food-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 15px;
  padding-left: 15px;
}

.food-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 12px;
  max-height: 280px;
  overflow-y: auto;
  padding: 8px;
  background: #f9f9f9;
  border-radius: 8px;
}

.food-cell {
  padding: 10px 8px;
  border: 1px solid #e8e8e8;
  border-radius: 6px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;
  background: #fff;
}

.food-cell-img {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 6px;
  margin: 0 auto 8px;
  display: block;
  transition: opacity 0.3s ease;
  opacity: 0;
}

.food-cell-img.loaded {
  opacity: 1;
}

.food-cell-img-placeholder {
  background: #ffedd5;
  color: #ea580c;
  font-size: 11px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 60px;
  border-radius: 6px;
  margin: 0 auto 8px;
}

.food-cell:hover {
  border-color: #f97316;
  background: #fff7ed;
}

.food-cell.selected {
  border-color: #f97316;
  background: linear-gradient(135deg, rgba(249, 115, 22, 0.1) 0%, rgba(234, 88, 12, 0.1) 100%);
  color: #333;
}

.food-cell.selected .food-cell-cal {
  color: #ea580c;
}

.food-cell.selected .food-cell-img-placeholder {
  background: #f97316;
  color: #fff;
}

.food-cell-name {
  font-size: 13px;
  font-weight: 600;
  margin-bottom: 4px;
}

.food-cell-cal {
  font-size: 11px;
  color: #999;
}

.nutrition-info {
  background: #f9f9f9;
  padding: 15px;
  border-radius: 8px;
}

.nutrition-title {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #333;
}

.nutri-item {
  background: #fff;
  padding: 8px;
  border-radius: 6px;
  text-align: center;
}

.nutri-label {
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
}

.nutri-value {
  font-size: 14px;
  font-weight: 600;
  color: #409EFF;
}

.weight-input-area {
  margin-top: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.weight-label {
  font-size: 13px;
  color: #666;
}

.weight-unit {
  font-size: 13px;
  color: #666;
}

.recipe-detail {
  padding: 20px 0;
}

.detail-header {
  margin-bottom: 24px;
}

.detail-header h2 {
  margin: 0 0 8px 0;
  color: #333;
}

.health-goal {
  color: #67c23a;
  font-weight: 500;
  margin: 0;
}

.detail-description {
  margin-bottom: 24px;
}

.detail-description h3 {
  margin: 0 0 8px 0;
  color: #333;
}

.nutrition-section {
  margin-bottom: 24px;
}

.nutrition-section h3 {
  margin: 0 0 16px 0;
  color: #333;
}

.nutrition-item {
  background: #f5f7fa;
  padding: 12px;
  border-radius: 8px;
  text-align: center;
}

.nutrition-label {
  font-size: 14px;
  color: #999;
  margin-bottom: 4px;
}

.nutrition-value {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.meal-section {
  margin-bottom: 24px;
}

.meal-section h3 {
  margin: 0 0 16px 0;
  color: #333;
}

.search-box {
  margin-bottom: 16px;
}

.dialog-content {
  padding: 20px 0;
}

.category-tabs {
  margin-bottom: 20px;
}

.food-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 16px;
  margin-top: 16px;
}

.food-card {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 12px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.food-card:hover {
  border-color: #409eff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.food-image {
  width: 80px;
  height: 80px;
  margin: 0 auto 8px;
  border-radius: 8px;
  overflow: hidden;
}

.food-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.food-image.placeholder {
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
}

.placeholder-icon {
  font-size: 24px;
  color: #999;
}

.food-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.selected-food {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
}

.selected-info {
  background: #f5f7fa;
  padding: 16px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.selected-name {
  flex: 1;
  font-weight: 500;
  color: #333;
}

.selected-weight {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 评论模块样式 */
.review-section {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.review-section h3 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 15px;
}

.review-input {
  margin-bottom: 20px;
}

.review-input .el-rate {
  margin-bottom: 10px;
}

.review-input .el-textarea {
  margin-bottom: 10px;
}

.review-item {
  padding: 15px;
  border-bottom: 1px solid #eee;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.review-header .user {
  font-weight: 500;
  color: #333;
}

.review-header-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.delete-btn {
  color: #f56c6c !important;
  font-size: 12px;
}

.delete-btn:hover {
  color: #f78989 !important;
}

.review-content {
  margin: 10px 0;
  color: #666;
  line-height: 1.5;
}

.review-time {
  font-size: 12px;
  color: #999;
}
</style>