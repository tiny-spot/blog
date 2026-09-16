<template>
  <div class="rich-editor">
    <Toolbar
      :editor="editorRef"
      :defaultConfig="toolbarConfig"
      mode="default"
      class="toolbar"
    />
    <Editor
      :model-value="modelValue"
      :defaultConfig="editorConfig"
      mode="default"
      class="editor"
      @onCreated="handleCreated"
      @onChange="handleChange"
    />
  </div>
</template>

<script setup>
import '@wangeditor/editor/dist/css/style.css'
import { onBeforeUnmount, shallowRef } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

defineProps({
  modelValue: { type: String, default: '' }
})
const emit = defineEmits(['update:modelValue'])

const editorRef = shallowRef()

const toolbarConfig = {
  excludeKeys: ['group-video', 'insertVideo']
}

const editorConfig = {
  placeholder: '请输入正文...',
  MENU_CONF: {
    uploadImage: {
      server: '/api/admin/upload/image',
      fieldName: 'file',
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      },
      customInsert(res, insertFn) {
        if (res.url) insertFn(res.url)
      }
    }
  }
}

const handleCreated = (editor) => {
  editorRef.value = editor
}

const handleChange = (editor) => {
  emit('update:modelValue', editor.getHtml())
}

onBeforeUnmount(() => {
  editorRef.value?.destroy()
})
</script>

<style scoped>
.rich-editor {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}
.toolbar {
  border-bottom: 1px solid #dcdfe6;
  background: #fafafa;
}
.editor {
  height: 400px;
  overflow-y: auto;
}
</style>
