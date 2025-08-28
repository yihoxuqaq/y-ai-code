<template>
  <BasicLayout />
</template>

<script setup lang="ts">
import BasicLayout from '@/layout/BasicLayout.vue'
import { healthCheck } from '@/api/healthController.ts'
healthCheck().then((res)=>{
  console.log(res.data)
})
</script>

<style scoped>

</style>
