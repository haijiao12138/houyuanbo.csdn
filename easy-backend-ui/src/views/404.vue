<template>
     <Error :status="404" :title="'糟糕，没有找到页面。。。'" />
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