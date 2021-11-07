<template>
     <Error :status="403" :title="'糟糕，没有权限访问页面。。。'" />
</template>

<script>
	import Error from '../components/Error/index.vue'
	
export default {
 components: {
    Error
  }
}
</script>

<style>

</style>