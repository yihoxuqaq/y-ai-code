<template>
  <BasicLayout />
</template>

<script setup lang="ts">
import BasicLayout from '@/layout/BasicLayout.vue'
import { healthCheck } from '@/api/healthController.ts'
import { useLoginUserStore } from '@/stores/loginUser.ts'
healthCheck().then((res)=>{
  console.log(res.data)
})
const loginUserStore=useLoginUserStore()
loginUserStore.fetchLoginUser()
</script>

<style scoped>

</style>
