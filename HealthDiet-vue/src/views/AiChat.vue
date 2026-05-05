<template>
  <div class="ai-chat-container">
    <div class="chat-header">
      <h2>AI健康助手</h2>
      <p class="subtitle">随时为您解答营养相关问题</p>
    </div>

    <div class="chat-messages" ref="messagesContainer">
      <!-- 消息列表 -->
      <div
        v-for="(msg, index) in messages"
        :key="index"
        class="message-wrapper"
        :class="msg.role"
      >
        <!-- ✅ 头像（已修改） -->
        <div class="avatar">
          <span v-if="msg.role === 'user'">👤</span>
          <img v-else src="/AiChat.png" class="ai-avatar" />
        </div>

        <div class="message-bubble">
          <!-- ✅ AI名称 -->
          <div v-if="msg.role === 'assistant'" class="ai-name">
            AI营养助手
          </div>

          <div class="message-content">{{ msg.content }}</div>
          <div class="message-time">{{ msg.time }}</div>
        </div>
      </div>

      <!-- ✅ loading状态（已改成图片头像） -->
      <div v-if="loading" class="message-wrapper assistant">
        <div class="avatar">
          <img src="/AiChat.png" class="ai-avatar" />
        </div>
        <div class="message-bubble">
          <div class="ai-name">AI营养助手</div>
          <div class="message-content typing">
            <span class="dot"></span>
            <span class="dot"></span>
            <span class="dot"></span>
          </div>
        </div>
      </div>
    </div>

    <div class="chat-input-wrapper">
      <div class="chat-input-container">
        <el-input
          v-model="inputMessage"
          type="textarea"
          :rows="3"
          placeholder="请输入您想咨询的营养问题..."
          resize="none"
          @keydown.enter.exact.prevent="sendMessage"
          @keydown.shift.enter.exact.prevent="addNewLine"
          :disabled="loading"
        />
        <el-button
          type="primary"
          class="send-btn"
          @click="sendMessage"
          :disabled="!inputMessage.trim() || loading"
        >
          发送
        </el-button>
      </div>
      <div class="input-hint">Enter 发送 | Shift + Enter 换行</div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import request from '../utils/request'

const formatTime = (date) => {
  return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
}

const messages = ref([
  {
    role: 'assistant',
    content:
      '您好！我是您的AI营养助手。我可以帮您解答关于营养搭配、饮食健康、热量计算等问题。有什么可以帮到您的吗？',
    time: formatTime(new Date())
  }
])

const inputMessage = ref('')
const loading = ref(false)
const messagesContainer = ref(null)

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop =
        messagesContainer.value.scrollHeight
    }
  })
}

const addNewLine = () => {
  inputMessage.value += '\n'
}

const sendMessage = async () => {
  const content = inputMessage.value.trim()
  if (!content || loading.value) return

  // 用户消息
  messages.value.push({
    role: 'user',
    content,
    time: formatTime(new Date())
  })

  inputMessage.value = ''
  loading.value = true
  scrollToBottom()

  try {
    const res = await request.post('/ai/ask', content)

    if (res.code === 200) {
      messages.value.push({
        role: 'assistant',
        content: res.data,
        time: formatTime(new Date())
      })
    } else {
      messages.value.push({
        role: 'assistant',
        content: '抱歉，服务出现问题，请稍后再试。',
        time: formatTime(new Date())
      })
    }
  } catch (error) {
    console.error('AI请求失败:', error)
    messages.value.push({
      role: 'assistant',
      content: 'AI服务暂时不可用，请检查网络连接。',
      time: formatTime(new Date())
    })
  } finally {
    loading.value = false
    scrollToBottom()
  }
}
</script>

<style scoped>
.ai-chat-container {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 120px);
  background: #f8fafc;
  border-radius: 12px;
  overflow: hidden;
}

.chat-header {
  background: linear-gradient(135deg, #fed7aa 0%, #fdba74 100%);
  color: #7c2d12;
  padding: 20px 24px;
  text-align: center;
}

.chat-header h2 {
  margin: 0 0 6px 0;
  font-size: 20px;
  font-weight: 600;
}

.subtitle {
  margin: 0;
  font-size: 13px;
  color: #9a3412;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  background: #fff;
}

.message-wrapper {
  display: flex;
  gap: 12px;
  max-width: 75%;
}

.message-wrapper.user {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.message-wrapper.assistant {
  align-self: flex-start;
}

/* ✅ 头像优化 */
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  flex-shrink: 0;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

/* ✅ AI头像 */
.ai-avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}

/* ✅ AI头像背景 */
.message-wrapper.assistant .avatar {
  background: linear-gradient(135deg, #fb923c, #f97316);
}

/* ✅ 用户头像背景 */
.message-wrapper.user .avatar {
  background: #e2e8f0;
}

.message-bubble {
  background: #f8fafc;
  border-radius: 14px;
  padding: 12px 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.message-wrapper.user .message-bubble {
  background: linear-gradient(135deg, #fed7aa 0%, #fb923c 100%);
  color: #7c2d12;
}

.message-content {
  font-size: 14px;
  line-height: 1.6;
  white-space: pre-wrap;
}

/* ✅ AI名称 */
.ai-name {
  font-size: 12px;
  color: #fb923c;
  margin-bottom: 4px;
}

.message-time {
  font-size: 11px;
  color: #999;
  margin-top: 4px;
  text-align: right;
}

/* typing动画 */
.typing {
  display: flex;
  gap: 4px;
}

.typing .dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #cbd5e1;
  animation: bounce 1.4s infinite ease-in-out both;
}

.typing .dot:nth-child(1) {
  animation-delay: -0.32s;
}
.typing .dot:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes bounce {
  0%,
  80%,
  100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1);
  }
}

.chat-input-wrapper {
  padding: 14px 20px;
  background: #fff;
}

.chat-input-container {
  display: flex;
  gap: 12px;
}

.send-btn {
  height: 72px;
  border-radius: 10px;
  background: linear-gradient(135deg, #fb923c, #f97316);
  border: none;
}

.input-hint {
  font-size: 11px;
  color: #adb5bd;
  margin-top: 6px;
  text-align: right;
}
</style>